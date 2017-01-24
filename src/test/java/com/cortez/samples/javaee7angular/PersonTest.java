package com.cortez.samples.javaee7angular;

import com.cortez.samples.javaee7angular.data.Person;
import static org.junit.Assert.*;
import static org.hamcrest.core.Is.is;



/**
 * Created by jr00n on 20/01/17.
 */
public class PersonTest {
    @org.junit.Test
    public void getsetId() throws Exception {
        Person person = new Person();
        person.setId(new Long(1));
        assertThat(person.getId(), is(new Long(1)));
    }

    @org.junit.Test
    public void getsetName() throws Exception {
        Person person = new Person();
        person.setName("jrOOn");
        assertThat(person.getName(), is(new String("jrOOn")));
    }


    @org.junit.Test
    public void getsetDescription() throws Exception {
        Person person = new Person();
        String description = new String("SuperMan");
        person.setDescription(description);
        assertThat(person.getDescription(),is(description));
    }


    @org.junit.Test
    public void getsetImageUrl() throws Exception {
        Person person = new Person();
        String url = new String("http://what.ever");
        person.setImageUrl(url);
        assertThat(person.getImageUrl(),is(url));
    }

    @org.junit.Test
    public void equals() throws Exception {
        Person personA = new Person();
        personA.setId(new Long(1));
        Person personB = new Person();
        personB.setId(new Long(2));
        assertThat(personA.equals(personB),is(false));
    }

    @org.junit.Test
    public void checkHashCode() throws Exception {
        Person person = new Person();
        person.setId(new Long(1));
        assertThat(person.hashCode(), is( 1));
    }

}