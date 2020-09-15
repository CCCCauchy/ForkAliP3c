package com.alibaba.p3c.pmd.lang.java.rule.xml;

import net.sourceforge.pmd.testframework.SimpleAggregatorTst;

/**
 * @desc: <br/>
 * @time: 2020/9/14 16:14<br/>
 * @author: Cauchy <br/>
 * @since: V1.0 <br/>
 */
public class XmlRulesTest extends SimpleAggregatorTst {

    public static final String RULESET = "java-ali-xml";

    @Override
    protected void setUp() {
        addRule(RULESET, "ColorNameRule");
    }
}
