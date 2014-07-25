package com.sk.cache.wrappers.region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlagGroup implements Flagger {

	private List<Flagger> flaggers = new ArrayList<Flagger>();

	public FlagGroup() {
	}
	
	public void add(Flagger... flaggers) {
		Collections.addAll(this.flaggers, flaggers);
	}

	@Override
	public void flag(Region r) {
		for (Flagger f : flaggers) {
			f.flag(r);
		}
	}

	@Override
	public void unflag(Region r) {
		for (Flagger f : flaggers) {
			f.unflag(r);
		}
	}
}
