ruleset {
    ruleset('rulesets/basic.xml')
    ruleset('rulesets/braces.xml') {
        'IfStatementBraces' enabled: false
        'ElseBlockBraces' enabled: false
    }
    ruleset('rulesets/concurrency.xml')
    ruleset('rulesets/convention.xml') {
        'NoDef' enabled: false
    }
    ruleset('rulesets/design.xml')
    ruleset('rulesets/dry.xml') {
        'DuplicateStringLiteral' ignoreStrings: ';,\\'
    }
    ruleset('rulesets/formatting.xml') {
        'SpaceAroundMapEntryColon' characterAfterColonRegex: /\s/
        'ClassJavadoc' enabled:false
    }
    ruleset('rulesets/generic.xml')
    ruleset('rulesets/groovyism.xml') {
        'GetterMethodCouldBeProperty' enabled: false
    }
    ruleset('rulesets/imports.xml') {
        'MisorderedStaticImports' enabled: false
        'NoWildcardImports' enabled: false
    }
    ruleset('rulesets/jdbc.xml')
    //ruleset('rulesets/junit.xml')
    ruleset('rulesets/logging.xml') {
        'Println' enabled: false
        'SystemOutPrint' enabled: false
    }
    ruleset('rulesets/naming.xml') {
        'VariableName' finalRegex: "[a-z][a-zA-Z0-9]*"
        'MethodName' regex: "[a-z][\\*a-zA-Z ,]*"
    }
    ruleset('rulesets/security.xml') {
        'JavaIoPackageAccess' enabled: false
        'SystemExit' enabled: false
        'JavaIoPackageAccess' enabled: false
    }
    //ruleset('rulesets/size.xml')
    ruleset('rulesets/unnecessary.xml') {
        // disabling due to code narc bug =>
        //http://sourceforge.net/tracker/?func=detail&atid=1126573&aid=3524882&group_id=250145
        'UnnecessaryPackageReference' enabled: false
        'UnnecessaryGString' enabled: false
        'UnnecessaryReturnKeyword' enabled: false
        //'UnnecessaryElseStatement' enabled: false
        'UnnecessaryCollectCall' enabled: false
    }
    ruleset('rulesets/unused.xml')
}
