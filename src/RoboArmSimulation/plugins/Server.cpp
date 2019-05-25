#define COMMAND_BUFFER_SIZE 2
#define LISTEN_PORT 52333
namespace roboarm
{

    class Server {
        public:
           int sockfd;
           int new_fd;
           struct sockaddr_in my_addr;
           struct sockaddr_in their_addr;
           socklen_t sin_size;
           int sock, length, n;
           socklen_t fromlen;
           struct sockaddr_in server;
           struct sockaddr_in from;
           int comAmpBuffer[ COMMAND_BUFFER_SIZE ] ;
           int port = LISTEN_PORT;

        public:Server(){}

        public:~Server(){
                close(sock);
        }

        public:void error(const char *msg)
        {
            std::cout << "Error="<< msg;
            exit(0);
        }

        public:void init()
        {
            sockfd = socket(AF_INET, SOCK_STREAM, 0);

            /* setting a non blocking connection. Did not work: Not reading data when this is set. See readme*/
            // fcntl(sockfd, F_SETFL, O_NONBLOCK);
            /* end of setting a non blocking connection*/

            my_addr.sin_family = AF_INET;
            my_addr.sin_port = htons(port);
            my_addr.sin_addr.s_addr = INADDR_ANY;
            memset(&(my_addr.sin_zero), 0, 8);

            bind(sockfd, (struct sockaddr *)&my_addr, sizeof(struct sockaddr));

            listen( sockfd, 1 ) ;

            sin_size = sizeof(struct sockaddr_in);

            tcflush( new_fd, 0 );
        }

        public:int receiveData(int *command, int *delta)
        {
            int receivedData =  recv( new_fd, this->comAmpBuffer, COMMAND_BUFFER_SIZE * sizeof(int), MSG_DONTWAIT) ;
            if(receivedData>0){
                *command = htonl( this->comAmpBuffer[0] ) ;
                *delta = htonl( this->comAmpBuffer[1] ) ;
            }
            if(receivedData == 0){
                acceptNewConn();
            }

            return receivedData;
        }

        public:void acceptNewConn()
        {
            new_fd = accept( sockfd, (struct sockaddr *)&their_addr, &sin_size );
        }

    };

}
