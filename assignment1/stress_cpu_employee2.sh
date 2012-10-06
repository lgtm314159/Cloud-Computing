#!/bin/bash

dd if=/dev/urandom of=/home/ubuntu/bigfile count=20 bs=1024k

while [ 1 ]
do
  md5sum /home/ubuntu/bigfile
done

