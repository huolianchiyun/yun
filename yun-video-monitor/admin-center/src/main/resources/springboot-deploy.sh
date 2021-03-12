#!/bin/bash

COMMAND=$1

JAR_NAME=$2

ENV_CONFIG=$3

function print_usage(){
cat <<'EOF'
Usage: springboot-deploy [选项]
    start    xxx.jar  [dev|test|prod]  ###启动
    stop     xxx.jar                   ###停止
    restart  xxx.jar  [dev|test|prod]  ###重启
    status   xxx.jar                   ###查看
EOF
}

case ${COMMAND} in
    "start")
            echo ================ ${JAR_NAME} server starting by ${ENV_CONFIG} environment configuration ...  ================
            pid=$(ps -ef | grep ${JAR_NAME} | grep -v grep | grep -v springboot-deploy.sh | awk '{print $2}')
            if [[ "$pid" ]]; then
              echo the server is not stopped, please stop the server and start.
            else
              nohup java -jar ${JAR_NAME}  --spring.profiles.active=${ENV_CONFIG} >/dev/null 2>&1 &
              echo ================ ${JAR_NAME} server starting success...  ================
            fi
        ;;
    "stop")
            echo ================ ${JAR_NAME} server stopping... ================
            pid=$(ps -ef | grep ${JAR_NAME} | grep -v grep | grep -v springboot-deploy.sh | awk '{print $2}')
            if [[ "$pid" ]]; then
              ps -ef | grep ${JAR_NAME} | grep -v grep | grep -v springboot-deploy.sh | awk '{print $2}'| xargs kill -9
              sleep 1
              echo ================ ${JAR_NAME} server stopping success ================
            else
              echo the server has stopped, no need to stop.
            fi
        ;;
    "restart")
            echo ================ ${JAR_NAME} server stopping... ================
            pid=$(ps -ef | grep ${JAR_NAME} | grep -v grep | grep -v springboot-deploy.sh | awk '{print $2}')
            if [[ "$pid" ]]; then
              ps -ef | grep ${JAR_NAME} | grep -v grep | grep -v springboot-deploy.sh | awk '{print $2}'| xargs kill -9
              sleep 2
            fi

            echo ================ ${JAR_NAME} server restarting by ${ENV_CONFIG} environment configuration ... ================
            nohup java -jar ${JAR_NAME}  --spring.profiles.active=${ENV_CONFIG} >/dev/null 2>&1 &
            echo ================ ${JAR_NAME} server restarting success ================
        ;;
    "status")
           ps -ef | grep ${JAR_NAME} | grep -v grep
        ;;
    *)
        print_usage
        ;;
esac
