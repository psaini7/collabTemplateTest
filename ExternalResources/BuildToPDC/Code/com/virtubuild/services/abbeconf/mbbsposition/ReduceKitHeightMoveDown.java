package com.virtubuild.services.abbeconf.mbbsposition;

import com.virtubuild.core.api.Configuration;
import com.virtubuild.core.complete.CompleterSkeleton;
import com.virtubuild.services.abbeconf.mbbsposition.MbbsN185MoveDownAutoCompleterManager;

public class ReduceKitHeightMoveDown extends CompleterSkeleton {

	private Configuration configuration;
	private MbbsN185MoveDownAutoCompleterManager autoManager;

	@Override
	public boolean doComplete() {
		initAutoManager();
		autoManager.loadKits(configuration);
		return true;
	}

	@Override
	public boolean doCompleteSupported() {
		return true;
	}

	@Override
	protected void init() {
		super.init();
	}

	// get all components as a list
	private void initAutoManager() {
		configuration = getConfiguration();
		autoManager = new MbbsN185MoveDownAutoCompleterManager();
	}

}
