FROM sunshower/sunshower-base:1.0.0
ENV BRANCH_SPEC "1.0.0/master"
ENV PROJECT_NAME=sunshower-kernel

RUN git clone https://github.com/josiahhaswell/pf4j.git
RUN mvn clean install -f pf4j
RUN mkdir -p /home/sunshower/$PROJECT_NAME
COPY . /home/sunshower/$PROJECT_NAME
WORKDIR /home/sunshower/$PROJECT_NAME
RUN chmod +x ./scripts/build.sh 
ENTRYPOINT ./scripts/build.sh  $BRANCH_SPEC
