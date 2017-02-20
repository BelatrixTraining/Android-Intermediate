# View Pager and Tabs

`ViewPager` es un widget en el cual cada hijo es una vista independiente. Para añadir un `ViewPager` a nuestro layout es necesario el `<ViewPager>` a nuestro XML.
Si tenemos un diseño en el cada cada página de nuestro ViewPager consumira todo el ancho y alto de la pantalla entonces el layout XML se vería así:

```xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.v4.view.ViewPager
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/pager"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

Para agregar hijos que representen cada página se necesita conectar el ViewPager a un PagerAdapter. Existen dos tipos de Pager Adapter:

 - `FragmentPagerAdapter`: Es recomendado usar este tipo de adapter cuando se navega entre pantallas hermanas que representan un numero pequeño de páginas.
 - `FragmentStatePagerAdapter`: Es recomendad para cuando se tiene una colección de páginas de tamaño indeterminado. Va destruyendo los fragmentos conforme el usuario navega a otras páginas minimizando así el uso de memoria.

Ejemplo de un `FragmentStatePagerAdapter`:

```java
public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
    public DemoCollectionPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new DemoObjectFragment();
        Bundle args = new Bundle();
        // Our object is just an integer :-P
        args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 100;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "OBJECT " + (position + 1);
    }
}
```

##Agregar tabs al actionBar
Para crear tabs usando el `ActionBar` se necesita habilitar NAVIGATION_MODE_TABS, luego crear las instancias de `ActionBar.Tab` e implementar el `ActionBar.TabListener` para cada una. Por ejemplo, en el `onCreate` de nuestra actividad podemos tener algo similar al siguiente código:

```java
@Override
public void onCreate(Bundle savedInstanceState) {
    final ActionBar actionBar = getActionBar();
    ...

    // Specify that tabs should be displayed in the action bar.
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

    // Create a tab listener that is called when the user changes tabs.
    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // show the given tab
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // hide the given tab
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // probably ignore this event
        }
    };

    // Add 3 tabs, specifying the tab's text and TabListener
    for (int i = 0; i < 3; i++) {
        actionBar.addTab(
                actionBar.newTab()
                        .setText("Tab " + (i + 1))
                        .setTabListener(tabListener));
    }
}
```

##Cambiar de tab haciendo un gesto *swipe*
Para cambiar de página en un `ViewPager` cuando el usuario selecciona una tab, se necesita implementar el `ActionBar.TabListener` para seleccionar la página apropiada llamando al `setCurrentItem()` del `ViewPager`:

```java
@Override
public void onCreate(Bundle savedInstanceState) {
    ...

    // Create a tab listener that is called when the user changes tabs.
    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // When the tab is selected, switch to the
            // corresponding page in the ViewPager.
            mViewPager.setCurrentItem(tab.getPosition());
        }
        ...
    };
}
```

Por otro lado, se debe seleccionar la tab correspondiente cuando el usuario navega mediante el gesto *swipe*

```java
@Override
public void onCreate(Bundle savedInstanceState) {
    ...

    mViewPager = (ViewPager) findViewById(R.id.pager);
    mViewPager.setOnPageChangeListener(
            new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    // When swiping between pages, select the
                    // corresponding tab.
                    getActionBar().setSelectedNavigationItem(position);
                }
            });
    ...
}
```


##Referencias

 - https://material.io/guidelines/components/tabs.html
 - https://developer.android.com/training/implementing-navigation/lateral.html
 - https://developer.android.com/design/patterns/swipe-views.html
 - https://developer.android.com/training/animation/screen-slide.html
