/* 
 * Copyright (c) 2010, NHIN Direct Project
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright 
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.  
 * 3. Neither the name of the the NHIN Direct Project (nhindirect.org)
 *    nor the names of its contributors may be used to endorse or promote products 
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.nhind.xdr.config;

import org.nhind.config.rest.SettingService;
import org.nhindirect.config.model.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration class for XD related operations.
 * 
 * @author beau
 */
public class XdConfig
{
	private static final Logger LOGGER = LoggerFactory.getLogger(XdConfig.class);	
    
    protected SettingService settingService;
    
    /**
     * Enumeration representing valid XD settings and default values.
     * 
     * @author beau
     */
    private enum Settings
    {
        MAIL_HOST("xd.MailHost", "localhost"), 
        MAIL_USER("xd.MailUser", "direct"), 
        MAIL_PASS("xd.MailPass"), 
        AUDIT_METHOD("xd.AuditMethod", "file"), 
        AUDIT_HOST("xd.AuditHost"), 
        AUDIT_PORT("xd.AuditPort"), 
        AUDIT_FILE("xd.AuditFile", "./auditFile.txt");

        private String key;
        private String dflt;

        private Settings(String key)
        {
            this.key = key;
        }

        private Settings(String key, String dflt)
        {
            this(key);
            this.dflt = dflt;
        }

        /**
         * Return the value of key.
         * 
         * @return the value of key.
         */
        public String getKey()
        {
            return key;
        }

        /**
         * Return the value of dflt.
         * 
         * @return the value of dflt.
         */
        public String getDefault()
        {
            return dflt;
        }
    }

    private String mailHost;
    private String mailUser;
    private String mailPass;
    private String auditMethod;
    private String auditHost;
    private String auditPort;
    private String auditFile;

    /**
     * Default constructor.
     */
    public XdConfig()
    {
        this.init();
    }

    /**
     * Constructor which takes a ConfigurationServiceProxy.
     * 
     * @param proxy
     *            The ConfigurationServiceProxy object.
     */
    public XdConfig(SettingService settingService)
    {
        this.settingService = settingService;
        this.init();
    }

    /**
     * Initialize the configuration object.
     */
    public void init()
    {
        if (settingService == null)
            return;

        try
        {
            mailHost = getValue(Settings.MAIL_HOST);
            mailUser = getValue(Settings.MAIL_USER);
            mailPass = getValue(Settings.MAIL_PASS);
            auditMethod = getValue(Settings.AUDIT_METHOD);
            auditHost = getValue(Settings.AUDIT_HOST);
            auditPort = getValue(Settings.AUDIT_PORT);
            auditFile = getValue(Settings.AUDIT_FILE);
        }
        catch (Exception e)
        {
            LOGGER.error("Error initializing the XD configuration");
        }
    }
    
    private String getValue(Settings xdSetting) throws Exception
    {
        Setting tmp;
        String value = null;
        
        if (settingService != null)
        {
        	try
        	{
        		tmp = settingService.getSetting(xdSetting.getKey());
                if (tmp != null)
                    value = tmp.getValue();
                else
                    value = xdSetting.getDefault();
        	}
        	catch (Exception e)
        	{
        		LOGGER.warn("Could not find configured value for " + xdSetting.getKey() + ".  Defaulting to " + xdSetting.getDefault());
        		value = xdSetting.getDefault();
        	}

        }
        
        return value;
    }

    /**
     * @return the mailHost
     */
    public String getMailHost()
    {
        return mailHost;
    }

    /**
     * @param mailHost
     *            the mailHost to set
     */
    public void setMailHost(String mailHost)
    {
        this.mailHost = mailHost;
    }

    /**
     * @return the mailUser
     */
    public String getMailUser()
    {
        return mailUser;
    }

    /**
     * @param mailUser
     *            the mailUser to set
     */
    public void setMailUser(String mailUser)
    {
        this.mailUser = mailUser;
    }

    /**
     * @return the mailPass
     */
    public String getMailPass()
    {
        return mailPass;
    }

    /**
     * @param mailPass
     *            the mailPass to set
     */
    public void setMailPass(String mailPass)
    {
        this.mailPass = mailPass;
    }

    /**
     * @return the auditMethod
     */
    public String getAuditMethod()
    {
        return auditMethod;
    }

    /**
     * @param auditMethod
     *            the auditMethod to set
     */
    public void setAuditMethod(String auditMethod)
    {
        this.auditMethod = auditMethod;
    }

    /**
     * @return the auditHost
     */
    public String getAuditHost()
    {
        return auditHost;
    }

    /**
     * @param auditHost
     *            the auditHost to set
     */
    public void setAuditHost(String auditHost)
    {
        this.auditHost = auditHost;
    }

    /**
     * @return the auditPort
     */
    public String getAuditPort()
    {
        return auditPort;
    }

    /**
     * @param auditPort
     *            the auditPort to set
     */
    public void setAuditPort(String auditPort)
    {
        this.auditPort = auditPort;
    }

    /**
     * @return the auditFile
     */
    public String getAuditFile()
    {
        return auditFile;
    }

    /**
     * @param auditFile
     *            the auditFile to set
     */
    public void setAuditFile(String auditFile)
    {
        this.auditFile = auditFile;
    }

    /**
     * @return the settingService
     */
    public SettingService getSettingService()
    {
        return settingService;
    }

    /**
     * @param settingService
     *            the settingService to set
     */
    public void setSettingService(SettingService settingService)
    {
        this.settingService = settingService;
    }
}
