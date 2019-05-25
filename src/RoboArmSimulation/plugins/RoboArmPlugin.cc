#define DELTA_DIVISOR 1000.d

#include <gazebo/gazebo.hh>
#include <gazebo/physics/physics.hh>
#include <gazebo/transport/transport.hh>
#include <gazebo/msgs/msgs.hh>

#include "Server.cpp"

namespace gazebo
{
  /// \brief A plugin to control the RoboArm 
  class RoboArmPlugin : public ModelPlugin
  {
    /// \brief Constructor
    public: RoboArmPlugin() {}

    /// \brief The load function is called by Gazebo when the plugin is
    /// inserted into simulation
    /// \param[in] _model A pointer to the model that this plugin is
    /// attached to.
    /// \param[in] _sdf A pointer to the plugin's SDF element.
    public: virtual void Load(physics::ModelPtr _model, sdf::ElementPtr _sdf)
    {
      // Safety check
      if (_model->GetJointCount() == 3)
      {
        std::cerr << "Invalid joint count, RoboArm plugin not loaded\n";
        return;
      }

      // Store the model pointer for convenience.
      this->model = _model;

      // Get the joints. 
	  this->joint1 = _model->GetJoint("my_robo_arm::roboarm1::shape1_revolute");
	  this->joint2 = _model->GetJoint("my_robo_arm::roboarm1::shape2_revolute");
	  this->joint3 = _model->GetJoint("my_robo_arm::roboarm1::shape3_revolute");

      // Setup the P-controllers, with a gain of 0.1.
      this->pid1 = common::PID(0, 0, 0.1);
	  this->pid2 = common::PID(0, 0, 0.1);
	  this->pid3 = common::PID(0, 0, 0.1);

      // Apply the P-controller to the joints.
      // this->model->GetJointController()->SetVelocityPID(this->joint->GetScopedName(), this->pid);
	  this->model->GetJointController()->SetPositionPID(this->joint1->GetScopedName(), this->pid1);
	  this->model->GetJointController()->SetPositionPID(this->joint2->GetScopedName(), this->pid2);
	  this->model->GetJointController()->SetPositionPID(this->joint3->GetScopedName(), this->pid3);

      this->InitJointsPosition();

      // Create the node
      this->node = transport::NodePtr(new transport::Node());
      this->node->Init(this->model->GetWorld()->Name());

      // Create a topic name
      std::string topicName = "~/remote_ctrl_robo_arm/pos_cmd";

      // Subscribe to the topic, and register a callback
      // this->sub = this->node->Subscribe(topicName, &RoboArmPlugin::OnMsg, this);

      // Create the server to listen for commands
      server = roboarm::Server();

      this->sub = this->node->Subscribe(topicName, &RoboArmPlugin::OnMsg, this);

      // Listen to the update event. This event is broadcast every simulation iteration.
      this->updateConnection = event::Events::ConnectWorldUpdateBegin(boost::bind(&RoboArmPlugin::OnUpdate, this, _1));
    }

    public: void OnUpdate(const common::UpdateInfo & /*_info*/)
    {
        int command;
        int delta;
        int receivedData;
        if(listening == 0){
            server.init();
            server.acceptNewConn();
            listening = 1;
            std::cout << "listening" <<"\n";
             std::cout.flush();
        }

        receivedData = server.receiveData(&command, &delta);
        if(receivedData > 0){
            std::cout << "Received Data " << command << " " << delta <<"\n";
            this->DeltaPosition(command, delta);
        }
    }


    /// \brief Set the new position of the joint
    /// \param[in] jointNumber id of the joint to change
    /// \param[in] _deltaPos Change of the position
    public: void DeltaPosition(const int jointNumber, const int deltaPos)
    {
		physics::JointPtr joint;
		// As of now, we can just read int from the network
		// TODO: Fix when the server can read this as a double
		double deltaPosProcessed = (double)deltaPos / DELTA_DIVISOR;
		switch(jointNumber)
		{
			case 1:
			        this->positionJoint1 += deltaPosProcessed;
					this->model->GetJointController()->SetJointPosition(this->joint1, this->positionJoint1);

					break;
			case 2:
					this->positionJoint2 += deltaPosProcessed;
					this->model->GetJointController()->SetJointPosition(this->joint2, this->positionJoint2);
					break;
			case 3:
					this->positionJoint3 += deltaPosProcessed;
					this->model->GetJointController()->SetJointPosition(this->joint3, this->positionJoint3);
					break;
		}
		
    }

    /// \brief Init the position of the joint
    public: void InitJointsPosition()
    {
		// Set the joint's target position.
    	this->model->GetJointController()->SetJointPosition( this->joint1, 0);
		// Set the joint's target position.
    	this->model->GetJointController()->SetJointPosition( this->joint2, 0);
		// Set the joint's target position.
    	this->model->GetJointController()->SetJointPosition( this->joint3, 0);
    }


    /// \brief Handle incoming message
    /// \param[in] _msg Repurpose a vector3 message.
    private: void OnMsg(ConstVector3dPtr &_msg)
    {
		std::cout << "Message received. Joint number:" << _msg->x() << " Delta: "<< _msg->y() <<"\n";
		 this->DeltaPosition(_msg->x(), _msg->y());
    }

    /// \brief A node used for transport
    private: transport::NodePtr node;

    /// \brief A subscriber to a named topic.
    private: transport::SubscriberPtr sub;

    /// \brief Pointer to the model.
    private: physics::ModelPtr model;

    /// \brief Pointer to the joint.
    private: physics::JointPtr joint1;

	/// \brief Pointer to the joint.
    private: physics::JointPtr joint2;

	/// \brief Pointer to the joint.
    private: physics::JointPtr joint3;

    /// \brief A PID controller for the joint.
    private: common::PID pid1;

    /// \brief A PID controller for the joint.
    private: common::PID pid2;

    /// \brief A PID controller for the joint.
    private: common::PID pid3;
	
	///
	private: double positionJoint1 = 0;
	private: double positionJoint2 = 0;
	private: double positionJoint3 = 0;

	private: roboarm::Server server;
    private: int listening = 0;


    // Pointer to the update event connection
    private: event::ConnectionPtr updateConnection;

  };

  // Tell Gazebo about this plugin, so that Gazebo can call Load on this plugin.
  GZ_REGISTER_MODEL_PLUGIN(RoboArmPlugin)
}

