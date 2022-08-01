set -B                  # enable brace expansion
for i in {1..10}; do
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 1}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 2}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 3}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 4}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 5}]' &
done

for i in {1..10}; do
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 1}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 2}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 3}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 4}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 5}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 6}]' &
done

for i in {1..10}; do
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 1}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 2}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 3}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 4}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 5}]' &
  curl -X POST "http://localhost:8080/flows" -H 'Content-Type: application/json' -d '[{"src_app": "foo'"${i}"'", "dest_app": "bar", "vpc_id": "vpc-0", "bytes_tx":220, "bytes_rx": 150, "hour": 6}]' &
done
