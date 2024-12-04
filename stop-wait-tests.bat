echo Test 1
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5000 -outfile=outfiles/Test1-file1.pdf > test1-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5000 -packetsize=150 -f=file1.pdf > test1-client-output.txt

echo Test 2
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5001 -outfile=outfiles/Test2-file1.pdf > test2-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5001 -packetsize=200 -f=file1.pdf > test2-client-output.txt

echo Test 3
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5002 -outfile=outfiles/Test3-file1.pdf > test3-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5002 -packetsize=250 -f=file1.pdf > test3-client-output.txt

echo Test 4
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5003 -outfile=outfiles/Test4-file1.pdf > test4-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5003 -packetsize=300 -f=file1.pdf > test4-client-output.txt

echo Test 5
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5004 -outfile=outfiles/Test5-file1.pdf > test5-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5004 -packetsize=1000 -f=file1.pdf > test5-client-output.txt

echo Test 6
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5005 -outfile=outfiles/Test6-file1.pdf > test6-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=127.0.0.1 -port=5005 -packetsize=1500 -f=file1.pdf > test6-client-output.txt

echo Test 7
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5006 -outfile=outfiles/Test7-file2.txt > test7-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5006 -packetsize=152 -f=file2.txt > test7-client-output.txt

echo Test 8
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5007 -outfile=outfiles/Test8-file2.txt > test8-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5007 -packetsize=201 -f=file2.txt > test8-client-output.txt

echo Test 9
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5006 -outfile=outfiles/Test9-file2.txt > test9-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5006 -packetsize=352 -f=file2.txt > test9-client-output.txt

echo Test 10
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5005 -outfile=outfiles/Test10-file2.txt > test10-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5005 -packetsize=1790 -f=file2.txt > test10-client-output.txt

echo Test 11
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5004 -outfile=outfiles/Test11-file2.txt > test11-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5004 -packetsize=2020 -f=file2.txt > test11-client-output.txt

echo Test 12
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5003 -outfile=outfiles/Test12-file2.txt > test12-server-output.txt &
sleep 5
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5003 -packetsize=2048 -f=file2.txt > test12-client-output.txt

echo Check file contents
sha256sum -c outfiles-sums.sha256 > outfiles/sha256sum-results

echo Test 13 - Check incorrect server parameters
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5 -outfile=outfiles/Test12-file2.txt
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=200000 -outfile=outfiles/Test12-file2.txt
java -jar UdpStopWaitServer-5784.jar -ip=1.2.3.4.5 -port=5000 -outfile=outfiles/Test12-file2.txt
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=5.6 -outfile=outfiles/Test12-file2.txt

echo Test 14 - Check missing server parameters
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -outfile=outfiles/Test12-file2.txt
java -jar UdpStopWaitServer-5784.jar -port=123 -outfile=outfiles/Test12-file2.txt
java -jar UdpStopWaitServer-5784.jar -ip=0.0.0.0 -port=123

echo Test 15 - Check incorrect client parameters
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5 -packetsize=2048 -f=file2.txt
java -jar UdpStopWaitClient-5784.jar -dest=1.2.3.4.5 -port=5003 -packetsize=2048 -f=file2.txt
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5003 -packetsize=5.6 -f=file2.txt

echo Test 16 - Check missing client parameters
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5003 -packetsize=2048
java -jar UdpStopWaitClient-5784.jar -port=5003 -packetsize=2048 -f=file2.txt
java -jar UdpStopWaitClient-5784.jar -dest=localhost -packetsize=2048 -f=file2.txt
java -jar UdpStopWaitClient-5784.jar -dest=localhost -port=5003 -f=file2.txt
