package com.rains.proxy.server.demo;

import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import com.netflix.discovery.shared.Applications;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.Assert;

import java.beans.ConstructorProperties;
import java.net.URI;
import java.util.*;

public class EurekaNettyDiscoveryClient implements DiscoveryClient {
    public static final String DESCRIPTION = "Spring Cloud Eureka Discovery Client";
    private final EurekaInstanceConfig config;
    private final EurekaClient eurekaClient;

    public String description() {
        return "Spring Cloud Eureka Discovery Client";
    }

    public ServiceInstance getLocalServiceInstance() {
        return new ServiceInstance() {
            public String getServiceId() {
                return config.getAppname();
            }

            public String getHost() {
                return config.getHostName(false);
            }

            public int getPort() {
                return config.getNonSecurePort();
            }

            public boolean isSecure() {
                return config.getSecurePortEnabled();
            }

            public URI getUri() {
                return DefaultServiceInstance.getUri(this);
            }

            public Map<String, String> getMetadata() {
                return config.getMetadataMap();
            }
        };
    }

    public List<ServiceInstance> getInstances(String serviceId) {
        List<InstanceInfo> infos = this.eurekaClient.getInstancesByVipAddress(serviceId, false);
        List<ServiceInstance> instances = new ArrayList();
        Iterator var4 = infos.iterator();

        while(var4.hasNext()) {
            InstanceInfo info = (InstanceInfo)var4.next();
            instances.add(new EurekaServiceInstance(info));
        }

        return instances;
    }

    public List<String> getServices() {
        Applications applications = this.eurekaClient.getApplications();
        if (applications == null) {
            return Collections.emptyList();
        } else {
            List<Application> registered = applications.getRegisteredApplications();
            List<String> names = new ArrayList();
            Iterator var4 = registered.iterator();

            while(var4.hasNext()) {
                Application app = (Application)var4.next();
                if (!app.getInstances().isEmpty()) {
                    names.add(app.getName().toLowerCase());
                }
            }

            return names;
        }
    }

    @ConstructorProperties({"config", "eurekaClient"})
    public EurekaNettyDiscoveryClient(EurekaInstanceConfig config, EurekaClient eurekaClient) {
        this.config = config;
        this.eurekaClient = eurekaClient;
    }

    public static class EurekaServiceInstance implements ServiceInstance {
        private InstanceInfo instance;

        public EurekaServiceInstance(InstanceInfo instance) {
            Assert.notNull(instance, "Service instance required");
            this.instance = instance;
        }

        public InstanceInfo getInstanceInfo() {
            return this.instance;
        }

        public String getServiceId() {
            return this.instance.getAppName();
        }

        public String getHost() {
            return this.instance.getHostName();
        }

        public int getPort() {
            return this.isSecure() ? this.instance.getSecurePort() : this.instance.getPort();
        }

        public boolean isSecure() {
            return this.instance.isPortEnabled(InstanceInfo.PortType.SECURE);
        }

        public URI getUri() {
            return DefaultServiceInstance.getUri(this);
        }

        public Map<String, String> getMetadata() {
            return this.instance.getMetadata();
        }
    }
}
