#/bin/bash

sudo mkfs.ext4 /dev/xvdf
sudo mkdir -m 000 /ebs-vol
echo "/dev/xvdf /ebs-vol auto noatime 0 0" | sudo tee -a /etc/fstab
sudo mount /ebs-vol
echo "Hi there! How are you doing today?" > ~/hello.txt
sudo cp ~/hello.txt /ebs-vol/

