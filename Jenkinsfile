pipeline {
    environment {
        dockerimagename = "meenasathish/alliocdocker"
        dockerImage = ""
        PROJECT_ID = 'diesel-media-340214'
        CLUSTER_NAME = 'akubecluster'
        LOCATION = 'us-central1-b'
        CREDENTIALS_ID = 'k8s-new-jenkins-serviceaccount'
    }
    
    agent any
   
    stages {
        
        stage('Checkout Source') {
            steps {
                git branch: 'main', url: 'https://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@github.com/Allioc/tenant-service.git'
            }
        }
        
        stage ('Build Image') {
            steps {
                script {
                    dockerImage = docker.build dockerimagename
                }
            }
        }
        
        stage('Pushing Image') {
            environment {
                registryCredential = 'dockerhublogin'
            }
            steps {
                script {
                    docker.withRegistry( 'https://registry.hub.docker.com', registryCredential) {
                        dockerImage.push('tenantservice')
                    }
                }
            }
        } 
        
        stage('Deploy App to Kubernetes') {
            steps {
                step([$class: 'KubernetesEngineBuilder', projectId: env.PROJECT_ID, clusterName: env.CLUSTER_NAME, location: env.LOCATION, manifestPattern: 'deploymentservice.yml', credentialsId: env.CREDENTIALS_ID, verifyDeployments: true])
            }
        }
   }
}