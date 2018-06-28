package br.uefs.compiler.parser;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

public class GrammarTest {

    static Grammar grammar;

    @BeforeClass
    public static void buildGrammar() {
        grammar = new Grammar();

        grammar.addRule(new Rule("<E>", new Symbol.Array("<T>", "<E'>")));
        grammar.addRule(new Rule("<E'>", new Symbol.Array("'+'", "<T>", "<E'>")));
        grammar.addRule(new Rule("<E'>", new Symbol.Array("")));
        grammar.addRule(new Rule("<T>", new Symbol.Array("<F>", "<T'>")));
        grammar.addRule(new Rule("<T'>", new Symbol.Array("'*'", "<F>", "<T'>")));
        grammar.addRule(new Rule("<T'>", new Symbol.Array("")));
        grammar.addRule(new Rule("<F>", new Symbol.Array("'id'")));
        grammar.addRule(new Rule("<F>", new Symbol.Array("'('", "<E>", "')'")));
        grammar.setStartSymbol(new Symbol("<E>"));
    }

    @Test
    public void FirstTestE() {
        Symbol.Set set = new Symbol.Set();
        set.addAll(Arrays.asList(new Symbol("'('"), new Symbol("'id'")));
        Assert.assertEquals(set, grammar.first(new Symbol("<E>")));
    }

    @Test
    public void FirstTestT() {
        Symbol.Set set = new Symbol.Set();
        set.addAll(Arrays.asList(new Symbol("'('"), new Symbol("'id'")));
        Assert.assertEquals(set, grammar.first(new Symbol("<T>")));
    }

    @Test
    public void FirstTestF() {
        Symbol.Set set = new Symbol.Set();
        set.addAll(Arrays.asList(new Symbol("'('"), new Symbol("'id'")));
        Assert.assertEquals(set, grammar.first(new Symbol("<F>")));
    }

    @Test
    public void FirstTestEl() {
        Symbol.Set set = new Symbol.Set();
        set.addAll(Arrays.asList(new Symbol("'+'"), new Symbol("")));
        Assert.assertEquals(set, grammar.first(new Symbol("<E'>")));
    }

    @Test
    public void FirstTestTl() {
        Symbol.Set set = new Symbol.Set();
        set.addAll(Arrays.asList(new Symbol("'*'"), new Symbol("")));
        Assert.assertEquals(set, grammar.first(new Symbol("<T'>")));
    }

    @Test
    public void FollowTestE() {
        Symbol.Set set = new Symbol.Set();
        set.addAll(Arrays.asList(new Symbol("$"), new Symbol("')'")));
        Assert.assertEquals(set, grammar.follow(new Symbol("<E>")));
    }

    @Test
    public void FollowTestT() {
        Symbol.Set set = new Symbol.Set();
        set.addAll(Arrays.asList(new Symbol("'+'"),new Symbol("$"), new Symbol("')'")));
        Assert.assertEquals(set, grammar.follow(new Symbol("<T>")));
    }

    @Test
    public void FollowTestF() {
        Symbol.Set set = new Symbol.Set();
        set.addAll(Arrays.asList(new Symbol("'*'"), new Symbol("'+'"),new Symbol("$"), new Symbol("')'")));
        Assert.assertEquals(set, grammar.follow(new Symbol("<F>")));
    }

    @Test
    public void FollowTestEl() {
        Symbol.Set set = new Symbol.Set();
        set.addAll(Arrays.asList(new Symbol("$"), new Symbol("')'")));
        Assert.assertEquals(set, grammar.follow(new Symbol("<E'>")));
    }

    @Test
    public void FollowTestTl() {
        Symbol.Set set = new Symbol.Set();
        set.addAll(Arrays.asList(new Symbol("'+'"),new Symbol("$"), new Symbol("')'")));
        Assert.assertEquals(set, grammar.follow(new Symbol("<T'>")));
    }
}
