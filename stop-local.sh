#!/bin/bash

echo stopping create-loan / saga application
kill -9 $(cat create-loan.pid) && rm create-loan.pid

echo stopping loan service
kill -9 $(cat loan.pid) && rm loan.pid

echo stopping applicant service
kill -9 $(cat applicant.pid) && rm applicant.pid

echo stopping loan model service
kill -9 $(cat loan-model.pid) && rm loan-model.pid

echo stopping lra-coordinator
podman kill --cidfile lra-coordinator.cid
