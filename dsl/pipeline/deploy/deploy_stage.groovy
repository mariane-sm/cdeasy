job("deploy_to_stage") {

  deliveryPipelineConfiguration('Stage', 'Deploy to Stage')

  steps {
    shell 'sleep $((RANDOM%10+5))'
  }

  publishers {
    downstreamParameterized {
      trigger("e2e_test", 'SUCCESS', true, null) {
        currentBuild()
      }
    }
  }

  configure { project -> project / publishers << 'join.JoinTrigger' {
    'joinProjects'{}
    'joinPublishers' {
      'hudson.plugins.parameterizedtrigger.BuildTrigger' {
        'configs' {
          'hudson.plugins.parameterizedtrigger.BuildTriggerConfig' {
            'configs' {
              'hudson.plugins.parameterizedtrigger.CurrentBuildParameters' {}
            }
            projects('promote_rpm_to_production')
              condition('SUCCESS')
              triggerWithNoParameters('true')
          }
        }
      }
    }
    evenIfDownstreamUnstable('false')}
  }

}

