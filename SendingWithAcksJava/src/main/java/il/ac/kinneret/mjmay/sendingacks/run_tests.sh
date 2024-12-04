JAR_FILE="./SendingWithAcksJava-5785.jar"

# Test Cases
declare -a tests=(
  "-filesize=40B -packetsize=10B -acksize=10B -bandwidth=100mbps -rtt=52ms;0.208006"
  "-filesize=10KB -packetsize=10B -acksize=10B -bandwidth=100mbps -rtt=51ms;52.225638"
  "-filesize=23MB -packetsize=10B -acksize=10B -bandwidth=100mbps -rtt=53ms;127825.283760"
  "-filesize=45.5GB -packetsize=10B -acksize=10B -bandwidth=100mbps -rtt=154ms;752378713.040479"
  "-filesize=40B -packetsize=10B -acksize=10B -bandwidth=100mbps -rtt=45ms;0.180006"
  "-filesize=10KB -packetsize=1KB -acksize=10B -bandwidth=100mbps -rtt=32ms;0.320827"
  "-filesize=23MB -packetsize=52B -acksize=10B -bandwidth=100mbps -rtt=200ms;92761.100415"
  "-filesize=45.5GB -packetsize=0.1KB -acksize=10B -bandwidth=100mbps -rtt=16ms;7637923.381903"
  "-filesize=40B -packetsize=10B -acksize=10B -bandwidth=100mbps -rtt=11ms;0.044006"
  "-filesize=10KB -packetsize=1KB -acksize=10B -bandwidth=100Gbps -rtt=89ms;0.890001"
  "-filesize=23MB -packetsize=150B -acksize=10B -bandwidth=10.5Gbps -rtt=189ms;30387.817600"
  "-filesize=45.5GB -packetsize=0.1KB -acksize=10B -bandwidth=0.5Gbps -rtt=101ms;48188168.100381"
  "-filesize=40KB -packetsize=1KB -acksize=50B -bandwidth=10mbps -rtt=7ms;0.314368"
  "-filesize=10MB -packetsize=1000B -acksize=100B -bandwidth=100gbps -rtt=150ms;1572.900923"
  "-filesize=10MB -bandwidth=100gbps -rtt=50ms;Missing parameter. Expected output:
Usage: -filesize=f -packetsize=p -acksize=a -bandwidth=b -rtt=r
f can be of units B, KB, MB, GB
p can be of units B, KB
a can be of unit B
b can be of units mbps, gbps
r can be of unit ms"
  "-filesize=40B -packetsize=40B -rtt=40ms;Missing parameter. Expected output:
Usage: -filesize=f -packetsize=p -acksize=a -bandwidth=b -rtt=r
f can be of units B, KB, MB, GB
p can be of units B, KB
a can be of unit B
b can be of units mbps, gbps
r can be of unit ms"
)

tolerance=0.001

for test in "${tests[@]}"; do
  IFS=';' read -r input expected <<< "$test"
  
  echo "Running test: $input"
  output=$(java -jar "$JAR_FILE" $input)

  if [[ "$expected" == "Missing parameter."* ]]; then
    # Handle expected error outputs
    if [[ "$output" == *"$expected"* ]]; then
      echo "PASS: Correct error message for input '$input'."
    else
      echo "FAIL: Incorrect error message for input '$input'. Expected '$expected' but got '$output'."
    fi
  else
    # Handle numeric outputs
    actual=$(echo "$output" | grep -oE "[0-9]+\.[0-9]+")
    if [[ -z "$actual" ]]; then
      echo "FAIL: No numeric output detected. Expected $expected."
    else
      result=$(awk -v actual="$actual" -v expected="$expected" -v tol="$tolerance" '
      BEGIN {
        diff = (actual > expected) ? actual - expected : expected - actual;
        if (diff < tol) {
          printf "PASS: Expected %f got %f\n", expected, actual;
        } else {
          printf "FAIL: Expected %f but got %f (difference: %f)\n", expected, actual, diff;
        }
      }')

      echo "$result"
    fi
  fi
  echo "---------------------------"
done
