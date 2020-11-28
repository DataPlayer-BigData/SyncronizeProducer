kafka-topics.sh --create --zookeeper localhost:2181 --topic sync-producer --replication-factor 3 --partitions 3 --config min.insync.replicas=3

#kafka-topics.sh --describe --zookeeper localhost:2181 --topic sync-producer


