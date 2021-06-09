# drogue-ditto-converter project

This is a simple [Knative](https://knative.dev/) event payload converter from Drogue Cloud to Eclipse Ditto.

## Local execution
Make sure that `Java 11 SDK` is installed.

To start server locally run `mvn quarkus:dev`.
The command starts http server and automatically watches for changes of source code.
If source code changes the change will be propagated to running server. It also opens debugging port `5005`
so debugger can be attached if needed.

To run test locally run `mvn test`.

## The `func` CLI

It's recommended to set `FUNC_REGISTRY` environment variable.
```shell script
# replace ~/.bashrc by your shell rc file
# replace docker.io/johndoe with your registry
export FUNC_REGISTRY=docker.io/johndoe
echo "export FUNC_REGISTRY=docker.io/johndoe" >> ~/.bashrc
```

### Building

This command builds OCI image for the function.

```shell script
func build -v                  # build jar
func build --builder native -v # build native binary
```

### Running

This command runs the function locally in a container
using the image created above.
```shell script
func run
```

### Deploying

This commands will build and deploy the function into cluster.

```shell script
func deploy -v # also triggers build
FUNC_BUILD=false func deploy -v # without triggering the build
```

Deploy additional resources

```shell script
kubectl -n drogue-iot apply -f src/main/k8s/ditto-converter.yaml
```

## Function invocation

Do not forget to set `URL` variable to the route of your function.

You get the route by following command.
```shell script
func describe
```

### HTTPie

```shell script
URL=http://localhost:8080/
http -v ${URL} \
  Content-Type:application/json \
  Ce-Id:1 \
  Ce-Source:cloud-event-example \
  Ce-Type:dev.knative.example \
  Ce-Specversion:1.0 \
  Ce-Application:app \
  Ce-Device:device \
  Ce-Dataschema:ditto:simple-thing \
  temp=22.0 \
  hum=52.0
```

### Use with Drogue

```shell script
# Create Drogue Cloud resources
drg create app app_id
drg create device --app app_id simple-thing --data '{"credentials": {"credentials":[{ "pass": "foobar" }]}}'

# Create Ditto resources
echo "{}"  | http --auth ditto:ditto PUT http://$TWIN_API/api/2/things/app_id:simple-thing

# Send telemetry
http --auth simple-thing@app_id:foobar --verify build/certs/endpoints/ca-bundle.pem POST https://$HTTP_ENDPOINT/v1/foo data_schema==ditto:test temp:=23

# Check device state
http --auth ditto:ditto http://$TWIN_API/api/2/things/app_id:simple-thing
```
