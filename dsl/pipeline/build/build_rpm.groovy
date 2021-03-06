job("build_rpm") {

  deliveryPipelineConfiguration('Build', 'Build RPM')

  steps {
    shell 'sleep $((RANDOM%10+5))'
  }

  publishers {
    downstreamParameterized {
      trigger("promote_rpm_to_dev", 'SUCCESS', true, null) {
        currentBuild()
      }
    }
  }

}
