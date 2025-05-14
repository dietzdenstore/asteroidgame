import dietz.common.services.IGamePlugin;

module Common {
    requires java.desktop;
    requires javafx.graphics;

    exports dietz.common.services;
    exports dietz.common.data;
    exports dietz.common.data.asteroid;

    uses IGamePlugin;
}