/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.jenkov;

import java.util.concurrent.TimeUnit;

import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 4)
@Measurement(iterations = 8)
@Fork(value = 1)
public class MyBenchmark {

	public static void main(String[] args) throws RunnerException {

		Options opt = new OptionsBuilder().include(".*" + MyBenchmark.class.getSimpleName() + ".*").forks(1)
				.warmupIterations(4).measurementIterations(8).addProfiler(GCProfiler.class).build();

		new Runner(opt).run();
	}
	
	@Benchmark
	public void TestSeperateQueries(Parameters state, Blackhole bh) {

		try (Session session = state.driver.session(SessionConfig.forDatabase("twitter"))) {

			Result re22 = session.run("MATCH (u : User) RETURN u.screen_name As scname");

			while (re22.hasNext()) {
				Record record = re22.next();
				record.get("scname");
			}

			Result re2 = session.run("MATCH (u : User) RETURN u.name AS name");

			while (re2.hasNext()) {
				Record record = re2.next();
				record.get("name");
			}
		}
	}
	
	@Benchmark
	public void TestGeneralQuery(Parameters state, Blackhole bh) {

		try (Session session = state.driver.session(SessionConfig.forDatabase("twitter"))) {

			Result re = session.run("MATCH (u : User) RETURN u");

			while (re.hasNext()) {
				Record record = re.next();
				record.get("name");
				record.get("scname");
			}
		}
	}

	
	@Benchmark
	public void TestOneGoQuery(Parameters state, Blackhole bh) {

		try (Session session = state.driver.session(SessionConfig.forDatabase("twitter"))) {

			Result re3 = session.run("MATCH (u : User) RETURN u.name AS name, u.screen_name As scname");

			while (re3.hasNext()) {
				Record record = re3.next();
				record.get("name");
				record.get("scname");
			}
		}
	}
}
