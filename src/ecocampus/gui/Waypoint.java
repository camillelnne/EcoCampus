package ecocampus.gui;

import ecocampus.projection.PointCh;

/**
 * A waypoint (point on the map)
 */
public record Waypoint(PointCh position, Issue issue) {}
