package ru.vyarus.java.generics.resolver

import ru.vyarus.java.generics.resolver.context.GenericsInfo
import ru.vyarus.java.generics.resolver.support.Root
import spock.lang.Shared
import spock.lang.Specification

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future


/**
 * @author Vyacheslav Rusakov 
 * @since 18.11.2014
 */
class CacheTest extends Specification {

    @Shared
    ExecutorService executor

    void setupSpec() {
        executor = Executors.newFixedThreadPool(20)
    }

    void cleanupSpec() {
        executor.shutdown()
    }

    def "Check cache"() {

        when:
        GenericsInfo info = GenericsResolver.resolve(Root).genericsInfo
        then:
        info == GenericsResolver.resolve(Root).genericsInfo
    }

    def "Check concurrency"() {

        when: "Call finder in 20 threads"
        List<Future<?>> executed = []
        int times = 20
        times.times({
            executed << executor.submit({
                GenericsResolver.resolve(Root)
            })
        })
        // lock until finish
        executed.each({ it.get() })
        then: "Nothing fails"
        true
    }
}