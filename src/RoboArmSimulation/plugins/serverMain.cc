// File to verifiy the fuctionality of the server
#include "Server.cc"

int main(int _argc, char **_argv)
{
    RoboArm::Server *server = new RoboArm::Server();
    int command, delta, receivedData;
    server->init();
    do{
        receivedData = server->receiveData(&command, &delta);
        if(receivedData > 0){
            std::cout << "Received Data " << command << " " << delta <<"\n";
        }

        std::cout.flush();
    } while(receivedData>-1);
 std::cout << "c";
    return 0;
}