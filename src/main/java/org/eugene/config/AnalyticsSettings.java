package org.eugene.config;

import lombok.Getter;
import lombok.Setter;

public final class AnalyticsSettings {

  private AnalyticsSettings() {
	this.analyticsEnabled = false;
  }

  @Getter
  @Setter
  private boolean analyticsEnabled;

  public static AnalyticsSettings getInstance() {
	return SettingsHolder.INSTANCE;
  }

  private static class SettingsHolder {
	private static final AnalyticsSettings INSTANCE = new AnalyticsSettings();
  }
}
