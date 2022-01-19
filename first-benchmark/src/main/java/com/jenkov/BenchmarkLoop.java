package com.jenkov;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3)
@Measurement(iterations = 10)

public class BenchmarkLoop {

	 public static void main(String[] args) throws RunnerException {

	        Options opt = new OptionsBuilder()
	                .include(BenchmarkLoop.class.getSimpleName())
	                .forks(1)
	                .build();

	        new Runner(opt).run();
	    }
}
