package org.aivinog1.zeebe.experiment.grpc.load.balancing;

import io.camunda.zeebe.gateway.protocol.*;
import io.grpc.*;
import io.grpc.stub.*;

import java.io.*;
import java.security.*;
import java.util.*;

public class App {

    public static void main(String[] args) throws IOException, InterruptedException {
        final Server server = ServerBuilder.forPort(9000)
                .addService(new ZeebeGatewayService())
                .build();
        server.start();
        System.out.println("App is started");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("Initiate app shutdown");
            try {
                server.shutdown().awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println("App is shut");
        }));
        server.awaitTermination();
    }

    private static class ZeebeGatewayService extends GatewayGrpc.GatewayImplBase {

        private static final GatewayGrpc.GatewayBlockingStub CLIENT_0 = GatewayGrpc.newBlockingStub(ManagedChannelBuilder.forAddress("localhost", 26500).usePlaintext().build());
        private static final GatewayGrpc.GatewayBlockingStub CLIENT_1 = GatewayGrpc.newBlockingStub(ManagedChannelBuilder.forAddress("localhost", 27500).usePlaintext().build());
        private static final List<GatewayGrpc.GatewayBlockingStub> CLIENTS = List.of(CLIENT_0, CLIENT_1);
        private static final SecureRandom SECURE_RANDOM = new SecureRandom();

        @Override
        public void activateJobs(GatewayOuterClass.ActivateJobsRequest request, StreamObserver<GatewayOuterClass.ActivateJobsResponse> responseObserver) {
            final GatewayGrpc.GatewayBlockingStub chosenGateway = CLIENTS.get(Math.abs(request.getWorker().hashCode()) % CLIENTS.size());
            System.out.printf("We choose this client:%s%n", chosenGateway.toString());
            final Iterator<GatewayOuterClass.ActivateJobsResponse> activateJobsResponseIterator = chosenGateway.activateJobs(request);
            while (activateJobsResponseIterator.hasNext()) {
                final GatewayOuterClass.ActivateJobsResponse jobsResponse = activateJobsResponseIterator.next();
                responseObserver.onNext(jobsResponse);
            }
            responseObserver.onCompleted();
        }

        @Override
        public void cancelProcessInstance(GatewayOuterClass.CancelProcessInstanceRequest request, StreamObserver<GatewayOuterClass.CancelProcessInstanceResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).cancelProcessInstance(request));
            responseObserver.onCompleted();
        }

        @Override
        public void completeJob(GatewayOuterClass.CompleteJobRequest request, StreamObserver<GatewayOuterClass.CompleteJobResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).completeJob(request));
            responseObserver.onCompleted();
        }

        @Override
        public void createProcessInstance(GatewayOuterClass.CreateProcessInstanceRequest request, StreamObserver<GatewayOuterClass.CreateProcessInstanceResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).createProcessInstance(request));
            responseObserver.onCompleted();
        }

        @Override
        public void createProcessInstanceWithResult(GatewayOuterClass.CreateProcessInstanceWithResultRequest request, StreamObserver<GatewayOuterClass.CreateProcessInstanceWithResultResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).createProcessInstanceWithResult(request));
            responseObserver.onCompleted();
        }

        @Override
        public void deployProcess(GatewayOuterClass.DeployProcessRequest request, StreamObserver<GatewayOuterClass.DeployProcessResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).deployProcess(request));
            responseObserver.onCompleted();
        }

        @Override
        public void failJob(GatewayOuterClass.FailJobRequest request, StreamObserver<GatewayOuterClass.FailJobResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).failJob(request));
            responseObserver.onCompleted();
        }

        @Override
        public void throwError(GatewayOuterClass.ThrowErrorRequest request, StreamObserver<GatewayOuterClass.ThrowErrorResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).throwError(request));
            responseObserver.onCompleted();
        }

        @Override
        public void publishMessage(GatewayOuterClass.PublishMessageRequest request, StreamObserver<GatewayOuterClass.PublishMessageResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).publishMessage(request));
            responseObserver.onCompleted();
        }

        @Override
        public void resolveIncident(GatewayOuterClass.ResolveIncidentRequest request, StreamObserver<GatewayOuterClass.ResolveIncidentResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).resolveIncident(request));
            responseObserver.onCompleted();
        }

        @Override
        public void setVariables(GatewayOuterClass.SetVariablesRequest request, StreamObserver<GatewayOuterClass.SetVariablesResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).setVariables(request));
            responseObserver.onCompleted();
        }

        @Override
        public void topology(GatewayOuterClass.TopologyRequest request, StreamObserver<GatewayOuterClass.TopologyResponse> responseObserver) {
            System.out.printf("TOPOLOGY REQUEST: %s%n", request.toString());
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).topology(request));
            responseObserver.onCompleted();
        }

        @Override
        public void updateJobRetries(GatewayOuterClass.UpdateJobRetriesRequest request, StreamObserver<GatewayOuterClass.UpdateJobRetriesResponse> responseObserver) {
            responseObserver.onNext(CLIENTS.get(SECURE_RANDOM.nextInt(CLIENTS.size() - 1)).updateJobRetries(request));
            responseObserver.onCompleted();
        }
    }
}
