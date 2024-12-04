
# Network Transmission Simulator

## Overview
This project simulates network data transmission using the Stop-and-Wait protocol with acknowledgment packets. It includes:
1. **Theoretical Calculator**: Computes the time required to send a file under ideal conditions.
2. **UDP File Transfer Tools**: Sends files over a network using UDP and logs transmission details.
3. **Experimentation Framework**: Measures and compares theoretical vs. actual transmission times.

## Features
- Command-line interface for both theoretical calculations and actual file transmission.
- Logs packet transmission details, acknowledgments, and final results.
- Javadoc documentation for all classes and methods.
- Test cases for robust verification.

## Requirements
- **Java Development Kit (JDK)** version 8 or higher.
- Two computers on the same network for experimentation.
- Tools:
  - [iperf](https://iperf.fr/) for bandwidth measurement.
  - [Wireshark](https://www.wireshark.org/) for packet analysis.

---

## Theoretical Calculator: `SendingWithAcksJava.jar`

### Usage
Run the calculator with the following parameters:
```
java -jar SendingWithAcksJava.jar -filesize=<size> -packetsize=<size> -acksize=<size> -bandwidth=<bandwidth> -rtt=<time>
```

### Parameters
| Parameter    | Description                                  | Example          |
|--------------|----------------------------------------------|------------------|
| `filesize`   | Total file size to send (B, KB, MB, GB)      | `40B`, `10MB`    |
| `packetsize` | Size of the data packet (B, KB)              | `10B`, `1KB`     |
| `acksize`    | Size of the ACK packet (B)                   | `10B`            |
| `bandwidth`  | Bandwidth of the connection (mbps, gbps)     | `100mbps`        |
| `rtt`        | Round trip time between sender and receiver (ms) | `50ms`         |

### Example
```
java -jar SendingWithAcksJava.jar -filesize=40B -packetsize=10B -acksize=10B -bandwidth=100mbps -rtt=52ms
Output: 0.208006 s
```

---

## UDP File Transfer: `UdpStopWaitClient.jar` and `UdpStopWaitServer.jar`

### Sending Tool: `UdpStopWaitClient.jar`

#### Usage
```
java -jar UdpStopWaitClient.jar -dest=<IP> -port=<port> -f=<file> -packetsize=<size>
```

#### Parameters
| Parameter    | Description                                  | Example           |
|--------------|----------------------------------------------|-------------------|
| `dest`       | IP address of the receiving tool             | `127.0.0.1`       |
| `port`       | Port the receiving tool is listening on      | `5000`            |
| `f`          | Path to the file to be sent                 | `file.txt`        |
| `packetsize` | Size of each data packet in bytes            | `150`             |

#### Example
```
java -jar UdpStopWaitClient.jar -dest=127.0.0.1 -port=5000 -f=test.txt -packetsize=150
```

---

### Receiving Tool: `UdpStopWaitServer.jar`

#### Usage
```
java -jar UdpStopWaitServer.jar -ip=<IP> -port=<port> -outfile=<file>
```

#### Parameters
| Parameter  | Description                                  | Example           |
|------------|----------------------------------------------|-------------------|
| `ip`       | IP address to listen on                     | `0.0.0.0`         |
| `port`     | Port to listen on                           | `5000`            |
| `outfile`  | Path to the output file                     | `output.txt`      |

#### Example
```
java -jar UdpStopWaitServer.jar -ip=0.0.0.0 -port=5000 -outfile=received.txt
```

---

## Experiments
### Steps
1. **Setup**:
   - Use [iperf](https://iperf.fr/) to measure bandwidth.
   - Use `ping` to measure round-trip time (RTT).

2. **Run Experiments**:
   - Experiment with packet sizes (e.g., 100B, 1KB, 10KB).
   - Record outputs from tools and analyze results with Wireshark.

3. **Comparison**:
   - Compare theoretical results with actual results using the logs.

---

## Files and Logs
- `iperf.txt`: Bandwidth measurement results.
- `ping.txt`: RTT measurement results.
- Wireshark PCAP files for packet analysis.
- Logs for transmission details.

---

## Contribution
Feel free to submit issues or pull requests for enhancements and bug fixes.

