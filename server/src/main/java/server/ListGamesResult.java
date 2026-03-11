package server;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public record ListGamesResult(Collection<GameData> games){
}
