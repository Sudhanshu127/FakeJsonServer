# Dockerfile

RUN sudo apt-get update
RUN sudo apt-get install gradle
RUN sudo apt-get install make

RUN sudo apt-get install build-essential
RUN sudo apt-get install tcl8.5
RUN wget http://download.redis.io/releases/redis-stable.tar.gz
RUN tar xzf redis-stable.tar.gz
RUN cd redis-stable
RUN make
RUN sudo make install
RUN redis-server

RUN cd ..
RUN rm redis-stable.tar.gz

RUN git clone https://github.com/Sudhanshu127/FakeJsonServer.git
RUN cd FakeJsonServer/
RUN gradle :runApp