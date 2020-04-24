package albertgame.afengine.app;

import albertgame.afengine.app.message.MessageManager;
import java.util.HashMap;
import java.util.Map;

public class App {

    private static App instance = null;

    public static void launch(App app) {
        if (instance == null) {
            instance = app;
        }
        app.run();
    }

    public static void exit() {
        if (instance != null) {
            instance.isRunning = false;
        }
    }

    public static App getInstance() {
        return instance;
    }

    private final String appType;
    private String appName;
    private IAppLogic logic;
    private boolean isRunning;
    private final Map<String, String> settings = new HashMap<>();

    private final MessageManager msgManager;//消息管理器

    public App(String appType, String appName, IAppLogic logic) {
        this.appType = appType;
        this.appName = appName;
        this.logic = logic;
        isRunning = true;
        msgManager = MessageManager.getInstance();
        App.instance=this;
    }

    public App(String appName) {
        this("service", appName, null);
    }

    public IAppLogic getLogic() {
        return logic;
    }

    public void setLogic(IAppLogic logic) {
        this.logic = logic;
    }

    public String getAppType() {
        return appType;
    }

    public String getAppName() {
        return appName;
    }

    public Map<String, String> getSettings() {
        return settings;
    }

    public MessageManager getMsgManager() {
        return msgManager;
    }

    public void run() {

        if (initApp() == false) {
            System.out.println("The App :name[" + this.appName + "],type[" + this.appType + "] Init is Failed.");
            return;
        }
        if (this.logic != null) {
            if (this.logic.init() == false) {
                System.out.println("The AppLogic Init is Failed.");
                return;
            }
        }

        if (loop() == false) {
            System.out.println("The App :name[" + this.appName + "],type[" + this.appType + "] Loop is Failed.");
            return;
        }

        if (this.logic != null) {
            if (this.logic.shutdown() == false) {
                System.out.println("The AppLogic Shutdown is Failed.");
                return;
            }
        }

        if (shutdownApp() == false) {
            System.out.println("The App :name[" + this.appName + "],type[" + this.appType + "] Shutdown is Failed.");
            return;
        }

        System.out.println(this.appType + ":" + this.appName + ">Run And Exit Successfully...");
        System.exit(0);
    }

    private boolean loop() {

        long ctime = System.currentTimeMillis();
        long ntime, deltatime;
        while (isRunning) {
            ntime = System.currentTimeMillis();
            deltatime = ntime - ctime;
            ctime = ntime;

            if (logic != null) {
                if (logic.update(deltatime) == false) {
                    System.out.println("The AppLogic Update is Failed.");
                    return false;
                }
            }

            //update message manager
            this.msgManager.updateSendMessage(deltatime);

            //update app methods
            if (updateApp(deltatime) == false) {
                System.out.println("The App Update is Failed.");
                return false;
            }
        }
        return true;
    }

    //app  methods,if you want to get a special app,please override followed methods
    public boolean initApp() {
        return true;
    }

    public boolean updateApp(long time) {
        return true;
    }

    public boolean shutdownApp() {
        return true;
    }
}
