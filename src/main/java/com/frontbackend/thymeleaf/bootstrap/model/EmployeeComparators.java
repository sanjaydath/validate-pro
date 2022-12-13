package com.frontbackend.thymeleaf.bootstrap.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.frontbackend.thymeleaf.bootstrap.model.paging.Direction;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public final class EmployeeComparators {

    @EqualsAndHashCode
    @AllArgsConstructor
    @Getter
    static class Key {
        String name;
        Direction dir;
    }

    static Map<Key, Comparator<ChargeBackFile>> map = new HashMap<>();

	/*
	 * static { map.put(new Key("name", Direction.asc),
	 * Comparator.comparing(ChargeBackFile::getName)); map.put(new Key("name",
	 * Direction.desc), Comparator.comparing(ChargeBackFile::getName) .reversed());
	 * 
	 * map.put(new Key("start_date", Direction.asc),
	 * Comparator.comparing(ChargeBackFile::getStartDate)); map.put(new
	 * Key("start_date", Direction.desc),
	 * Comparator.comparing(ChargeBackFile::getStartDate) .reversed());
	 * 
	 * map.put(new Key("position", Direction.asc),
	 * Comparator.comparing(ChargeBackFile::getPosition)); map.put(new
	 * Key("position", Direction.desc),
	 * Comparator.comparing(ChargeBackFile::getPosition) .reversed());
	 * 
	 * map.put(new Key("salary", Direction.asc),
	 * Comparator.comparing(ChargeBackFile::getSalary)); map.put(new Key("salary",
	 * Direction.desc), Comparator.comparing(ChargeBackFile::getSalary)
	 * .reversed());
	 * 
	 * map.put(new Key("office", Direction.asc),
	 * Comparator.comparing(ChargeBackFile::getOffice)); map.put(new Key("office",
	 * Direction.desc), Comparator.comparing(ChargeBackFile::getOffice)
	 * .reversed());
	 * 
	 * map.put(new Key("extn", Direction.asc),
	 * Comparator.comparing(ChargeBackFile::getCount)); map.put(new Key("extn",
	 * Direction.desc), Comparator.comparing(ChargeBackFile::getCount) .reversed());
	 * }
	 */
    public static Comparator<ChargeBackFile> getComparator(String name, Direction dir) {
        return map.get(new Key(name, dir));
    }

    private EmployeeComparators() {
    }
}
