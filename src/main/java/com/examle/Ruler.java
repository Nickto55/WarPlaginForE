package com.examle;

import java.util.List;

public class Ruler {
    private String name;
    private List<String> teamNames;

    public Ruler(String name, List<String> teamNames) {
        this.name = name;
        this.teamNames = teamNames;
    }

    public String getName() {
        return name;
    }

    public List<String> getTeamNames() {
        return teamNames;
    }
}
