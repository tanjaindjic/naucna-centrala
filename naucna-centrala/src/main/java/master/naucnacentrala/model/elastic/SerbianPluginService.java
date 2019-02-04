/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package master.naucnacentrala.model.elastic;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
/**
 *
 * @author Milan Deket
 */
public class SerbianPluginService extends AbstractLifecycleComponent{

    @Inject
    public SerbianPluginService(Settings settings, SettingsFilter settingsFilter) {
        super(settings);
    }
        
    @Override
    protected void doStart() throws ElasticsearchException {
    	
    }

    @Override
    protected void doStop() throws ElasticsearchException {
    }

    @Override
    protected void doClose() throws ElasticsearchException {
    }

}
