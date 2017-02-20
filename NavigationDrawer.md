# Navigation Drawer

*Navigation Drawer* es un patrón de diseño, el cual permite manejar la navegación dentro de nuestra aplicación. Este patrón consta de un menú lateral izquierdo el cual se encuentra escondido por defecto pero aparece cuando el usuario hace un swipe desde el lado izquierdo de la pantalla o presiona el botón de menú. Este menú muestra las principales opciones de navegación de nuestra aplicación.

![navigation drawer pattern](https://github.com/BelatrixTraining/Android-Intermediate/blob/Lesson1/images/patterns_navigation_drawer.png)

## Tipos de Navigation Drawer

 - Permanent
 *Permanent Navigation Drawer* están siempre visibles y colocados en la izquierda. Se utiliza en desktop.
 - Persistent
*Persistent Navigation Drawer* se abrir o cerrar. Aparece cerrado por defector y es abierto cuando el usuario selecciona el icono de menú. Permanece abierto hasta que el usuario decide cerrarlo.
 - Mini Variant
Es una variante del *Persistent Navigation Drawer*. Se diferencia porque en su estado "cerrado" se puede seguir visualizando como una delgada barra lateral. Cuando este es abierto recupera su ancho natural.
 - Temporary
*Temporary Navigation Drawer* pueden ser abiertos o cerrados. Aparece cerrado por defecto, y cuando es abierto se muestra por encima del contenido hasta que una opción es seleccionada. Es el que suele usar en *smartphones* o *tablets*

![temporal navigation drawer closed](https://github.com/BelatrixTraining/Android-Intermediate/blob/Lesson1/images/patterns_navdrawer_behavior_temporary1.png)

![temporal navigation drawer opened](https://github.com/BelatrixTraining/Android-Intermediate/blob/Lesson1/images/patterns_navdrawer_behavior_temporary2.png)


##Como implementarlo?

###Definir el layout

Para agregar un *navigation drawer* a tu layout se debe declarar un `DrawerLayout` como vista raíz del diseño. Agregar dentro de el una vista para el contenido principal y otra vista con el contenido del *navigation drawer*.
Podemos ver un ejemplo de implementación en el siguiente código xml

```xml
<android.support.v4.widget.DrawerLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/drawer_layout"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <!-- La vista del contenido principal -->
    <FrameLayout
        android:id="@+id/content_frame"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
    <!-- El navigation drawer -->
    <ListView android:id="@+id/left_drawer"
        android:layout_width="240dp"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        android:choiceMode="singleChoice"
        android:divider="@android:color/transparent"
        android:dividerHeight="0dp"
        android:background="#111"/>
</android.support.v4.widget.DrawerLayout>
```

Luego de ver el ejemplo debemos identificar algunas características importantes:

 - La vista del contenido principal (`FrameLayout`) tiene que ser el primer campo dentro del `DrawerLayout` ya que la disposición XML implica un ordenamiento z y el panel lateral debe estar sobre el contenido.
 - La vista principal debe coincidir con el ancho y alto de la vista padre (`DrawerLayout`), ya que representa toda la UI cuando el *navigation drawer* esta oculto.
 - La vista del panel lateral (*navigationd drawer*) debe especificar su gravedad usando el atributo `android:layout_gravity` y con valor **start** en lugar de **left** para dar soporte a los idiomas con orientación de derecha a izquierda (RTL).
 - La vista del panel lateral especifica su ancho en **dp** y el alto debe coincidir con la vista padre. El ancho no debe superar los **320dp** para que el usuario siempre pueda ver una parte del contenido principal.

###Recibir eventos abiertos y cerrados
Para detectar los eventos cuando se abre y cierra el menú lateral debemos llamar al `setDrawarListener` y pasar una implementación de `DrawerLayout.DrawerListener` para sobreescribir los eventos de `onDrawerOpened` y `onDrawerClosened`.
Existe otra opción en caso nuestra actividad incluya un `ActionBar`  y es extender la clase `ActionBarDrawerToogle` la cual también implementa `DrawerLayout.DrawerListener`.

```java
public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ...

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ...

        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {

            /** Se llama cuando un drawer ha sido cerrado. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Se llama cuando el drawer fue abierto. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Se define el drawer toogle como el DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /* Se llama cada vez que deseamos pintar el menu */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Si el menu lateral esta abierto se ocultaran las acciones relacionadas al contenido
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }
}
```


## Referencias

 - https://material.io/guidelines/patterns/navigation-drawer.html#
 - https://developer.android.com/training/implementing-navigation/nav-drawer.html?hl=es-419
