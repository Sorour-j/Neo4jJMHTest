package com.jenkov;

import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

@State(Scope.Thread)
public class Parameters {
	public int a = 1;
    public int b = 2;
    public int sum ;
    
    public String uri; 
    public String username;
    public String password;
    public Driver driver;
    
    @Setup(Level.Trial)
    public void driverSetting() {
    	uri = "neo4j+s://demo.neo4jlabs.com:7687";
    	username = "twitter";
    	password = "twitter";
    }
    
    @Setup(Level.Invocation)
    public void setDriver() {
    	driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }
     
    @TearDown(Level.Invocation)
    public void disposeDriver() {
    	driver = null;
    }
}

