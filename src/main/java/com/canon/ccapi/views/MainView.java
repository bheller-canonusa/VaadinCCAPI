package com.canon.ccapi.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.shared.communication.PushMode;

import java.util.Optional;


@PageTitle("Main")
@Route(value = "")
public class MainView extends AppLayout {

    // private final Tabs menu;
    private H1 viewTitle;
    private final Tabs menu;



    @Override
    protected void afterNavigation() {
        super.afterNavigation();

        getTabForComponent(getContent()).ifPresent(v->menu.setSelectedTab(v));
                                                //.ifPresent(menu::setSelectedTab) is shorthand for v->menu.setSelectedTab(v)



        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle(){
        return getContent().getClass().getAnnotation(PageTitle.class).value();
    }

    private Optional<Tab> getTabForComponent(Component c){
       Optional<Tab> tt = menu.getChildren().filter(tab->ComponentUtil.getData(tab,Class.class).equals(c.getClass())).findFirst().map(Tab.class::cast);
        return tt;
    }

    private static Tab createTab(String text, Class<? extends Component> navigationTarget) {
        final Tab tab = new Tab();
        tab.add(new RouterLink(text, navigationTarget));
        ComponentUtil.setData(tab, Class.class, navigationTarget);
        return tab;
    }


    public Component[] createMenuItems() {
        return new Tab[]{
                createTab("Camera Status", CameraStatusView.class),
                createTab("Camera Settings", CameraSettingsView.class),
                createTab("Live View",LiveView.class)
        };
    }


    private Tabs createMenu() {
        final Tabs tabs = new Tabs();
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_MINIMAL);
        tabs.setId("tabs");
        tabs.add(createMenuItems());
        return tabs;
    }

    private Component createDrawerContent(Tabs menu){
        VerticalLayout vlayout = new VerticalLayout();

        vlayout.setSizeFull();
        vlayout.setPadding(false);
        vlayout.setSpacing(false);
        vlayout.getThemeList().set("dark", true);
        vlayout.getThemeList().set("spacing-s",true);
        vlayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setId("logo");
        logoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        logoLayout.add(new Image("images/logo.png","CCAPI Logo"));

        H1 drawtitle = new H1("CCAPI Controls");
        drawtitle.getStyle()
                .set("font-size", "var(--lumo-font-size-1)")
                .set("margin", "0");
       logoLayout.add(drawtitle);

       vlayout.add(logoLayout,menu);

        return vlayout;
    }



    private Component createHeaderContent() {
        HorizontalLayout layouthorizontal = new HorizontalLayout();

        layouthorizontal.setId("header");
        layouthorizontal.getThemeList().set("dark", true);
        layouthorizontal.setWidthFull();
        layouthorizontal.setSpacing(false);
        layouthorizontal.setAlignItems(FlexComponent.Alignment.CENTER);

        layouthorizontal.add(new DrawerToggle());

        viewTitle = new H1("CCAPI Demo");
        viewTitle.getStyle()
                .set("font-size", "var(--lumo-font-size-1)")
                .set("margin", "0");
        layouthorizontal.add(viewTitle);

        return layouthorizontal;
    }


    public MainView() {

        setPrimarySection(Section.DRAWER);

        addToNavbar(true, createHeaderContent());

        menu = createMenu();

        addToDrawer(createDrawerContent(menu));




    }
}



    /*
    private TextField name;
    private Button sayHello;

    public MainView() {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
        });

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello);
    }
*/

