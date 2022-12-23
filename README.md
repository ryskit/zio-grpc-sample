# ZIO gRPC Sample

## Run Server

```shell
sbt run
```

## Run Client

metadataを設定していないためエラーになる

```shell
sbt api-client/run
```

## Request with grpcurl

```shell
grpcurl -plaintext -rpc-header 'user-key: User1' -d '{ "name": "req1" }' -import-path ./contracts/contract-grpc-proto-interface/src/main/protobuf -proto greeter.proto localhost:9090 Greeter.Greet
```

## 参考

- https://scalapb.github.io/zio-grpc/
- https://github.com/scalapb/zio-grpc/tree/master/examples/fullapp
