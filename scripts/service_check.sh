#!/bin/bash
if  ! [ `docker images | grep none  | wc -l` -eq 0 ];then
docker rmi `docker images | grep none | awk '{print $3}'`
fi
