cd tuJmsClient
./compile_client.sh
cd ../tuJmsServer
./compile_server.sh
./run_server.sh &
cd ../tuJmsClient
./run_client.sh
