/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package master.naucnacentrala.model.elastic;

import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.settings.Settings;

/**
 *
 * @author Milan Deket
 */
public class SerbianPluginModule extends AbstractModule {
    private final Settings settings;
    
    public SerbianPluginModule(Settings settings) {
        this.settings = settings;
    }

    @Override
    protected void configure() {
		bind(SerbianPluginService.class).asEagerSingleton();
    }

	public Settings getSettings() {
		return settings;
	}

}
