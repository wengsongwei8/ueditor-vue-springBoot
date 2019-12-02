FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER wengzhonghui4@163.com

RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

RUN mkdir -p /apps
WORKDIR /apps
EXPOSE 6022
ADD ./target/ueditor-api.zip ./
RUN unzip ./ueditor-api.zip
CMD ["/apps/ueditor-api/bin/start.sh"]
