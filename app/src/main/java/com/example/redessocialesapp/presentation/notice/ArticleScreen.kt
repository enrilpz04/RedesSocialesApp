package com.example.redessocialesapp.presentation.notice

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.redessocialesapp.domain.model.Article
import com.example.redessocialesapp.presentation.ArticleCard
import com.example.redessocialesapp.presentation.ValidateLoginDialog
import com.example.redessocialesapp.ui.theme.AppColor
import com.example.redessocialesapp.ui.theme.poppinsFontFamily
import com.example.redessocialesapp.util.Screens
import com.google.accompanist.flowlayout.FlowRow
import java.util.Calendar
import java.util.Date

@Composable
fun ArticleScreen(navController: NavController, viewModel: ArticlesViewModel) {

    // Obtener el usuario
    val user by viewModel.user.collectAsState()

    // Instanciar al usuario al iniciar la pantalla
    LaunchedEffect(Unit) {
        viewModel.getUser()
    }

    // Obtener la lista de artículos
    val articles by viewModel.articles.collectAsState()

    // Obtener la lista de articulos favoritos
    val favArticles by viewModel.favArticles.collectAsState()

    // Variable para el estado de favoritos de las noticias
    val articleFavStatus = remember { mutableStateOf<Map<Article, Boolean>>(emptyMap()) }

    // Variable para el estado de las categorías disponibles
    val articlesCategories = remember { mutableStateOf(listOf<String>()) }

    // Variable para el estado de las localizaciones disponibles
    val articlesLocations = remember { mutableStateOf(listOf<String>()) }

    // Actualizar los estados de articulos favoritos
    LaunchedEffect(articles, favArticles) {
        articleFavStatus.value = articles.associateWith { article ->
            favArticles.contains(article)
        }
        articles.forEach { article ->
            if (!articlesCategories.value.contains(article.category)) {
                articlesCategories.value = articlesCategories.value + article.category
            }
        }
        articles.forEach { article ->
            if (!articlesLocations.value.contains(article.location)) {
                articlesLocations.value = articlesLocations.value + article.location
            }
        }
    }

    // Variable que almacena el índice de la pestaña seleccionada
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Obtener los articulos necesarios para cada elemento del menú de navegación
    LaunchedEffect(selectedTabIndex) {
        when (selectedTabIndex) {
            0 -> viewModel.getMostRecentArticles(10) // Para el tab "Recientes"
            1 -> viewModel.getMostPopularArticles(10) // Para el tab "Populares"
            articlesCategories.value.size + 2 -> println()
            else -> viewModel.getArticlesByCategory(articlesCategories.value[selectedTabIndex - 2]) // Para los tabs de categorías
        }
    }

    // Variable para el estado de la barra de búsqueda
    var searchText by remember { mutableStateOf("") }

    // Obtener los articulos necesarios mediante el texto introducido en la barra de búsqueda
    LaunchedEffect(searchText) {
        if (searchText.length >= 3) {
            viewModel.getArticlesByTitle(searchText)
            selectedTabIndex = articlesCategories.value.size + 2
        } else {
            viewModel.getMostRecentArticles(10)
            selectedTabIndex = 0
        }
    }

    // Variable para el estado del filtro
    var filter by remember { mutableStateOf(false) }

    // Variable para mostrar el diálogo de inicio de sesión
    val showDialog = remember { mutableStateOf(false) }

    // Mostrar el diálogo de inicio de sesión
    if (showDialog.value){
        ValidateLoginDialog(
            showDialog = showDialog.value,
            onDismissRequest = { showDialog.value = false },
            onRegister = { navController.navigate(Screens.SignUpScreen.route){
                popUpTo(Screens.NoticeScreen.route){
                    inclusive = false
                }
            } },
            onLogin = { navController.navigate(Screens.LoginScreen.route){
                popUpTo(Screens.NoticeScreen.route){
                    inclusive = false
                }
            } },
            text = "para añadir una noticia a favoritos."
        )
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // No mostrar la barra de búsqueda si se está filtrando
        if (!filter) {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Icono de búsqueda",
                        tint = Color.Gray
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            filter = !filter
                        }
                    ) {
                        Icon(
                            Icons.Default.FilterAlt,
                            contentDescription = "Icono de limpiar", tint = Color.Gray
                        )
                    }
                },
                maxLines = 1,
                placeholder = { Text("Buscar noticias") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = AppColor,
                    unfocusedBorderColor = Color.LightGray,
                    cursorColor = AppColor
                ),
            )
        }

        // Mostrar el filtro si se está filtrando
        if (filter) {
            FilterOptionsCard(
                navController = navController,
                articlesViewModel = viewModel,
                onDismissRequest = { filter = false }
            )
        }

        // Barra del menu de nagevación
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTabIndex])
                        .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                    color = AppColor, // Color de la línea
                    height = 2.dp// Grosor de la línea
                )
            }
        ) {

            // Pestañas de la TabRow para recientes y populares
            Tab(
                text = { Text("Recientes") },
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 })
            Tab(
                text = { Text("Populares") },
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 })

            // Pestañas de la TabRow para las categorías cargadas desde el ViewModel
            articlesCategories.value.forEachIndexed { index, item ->
                Tab(
                    text = { Text(item) },
                    selected = selectedTabIndex == index + 2,
                    onClick = { selectedTabIndex = index + 2 }
                )
            }

            // Pestaña de búsqueda personalizada
            Tab(
                text = { Text(text = "Búsqueda Personalizada") },
                selected = selectedTabIndex == articlesCategories.value.size + 2,
                onClick = { selectedTabIndex = articlesCategories.value.size + 2 })
        }
        // Lista de artículos cargados desde el ViewModel
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF0F0F1))
        ) {

            // Mostrar un mensaje si no hay noticias
            if (articles.isEmpty()) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "No se han encontrado noticias.",
                        fontSize = 20.sp,
                        fontFamily = poppinsFontFamily,
                        color = Color.Red
                    )
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "",
                        tint = Color.Red
                    )
                }

            } else {

                // Mostrar las noticias en una columna
                LazyColumn {
                    items(articles.size) { index ->
                        val isFav = articleFavStatus.value[articles[index]] ?: false

                        // Llamada al componente de tarjeta de artículo
                        ArticleCard(item = articles[index], isFav, !user.userId.isEmpty(), true, {
                            if (!user.userId.isEmpty()) {
                                Log.d("ArticLE", "Añadiendo articulo")
                                if (!isFav) {
                                    viewModel.putFavArticle(articles[index])
                                } else {
                                    viewModel.deleteFavArticle(articles[index].articleId)
                                }
                            } else {
                                showDialog.value = true
                            }
                        }, {
                            navController.navigate("detail_article_screen/${it.articleId}") {
                                popUpTo(Screens.NoticeScreen.route) {
                                    inclusive = false
                                }
                            }
                        })
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterOptionsCard(
    navController: NavController,
    articlesViewModel: ArticlesViewModel,
    onDismissRequest: () -> Unit
) {

    var startDate by rememberSaveable { mutableStateOf("") }
    var endDate by rememberSaveable { mutableStateOf("") }
    var selectedCategories by rememberSaveable { mutableStateOf(listOf<String>()) }
    var selectedLocations by rememberSaveable { mutableStateOf(listOf<String>()) }

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    LaunchedEffect(Unit) {
        sheetState.show()
    }

    ModalBottomSheetLayout(
        sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
        sheetContent = {


            Column(modifier = Modifier.padding(16.dp)) {

                // Filtrado de fechas
                Text(
                    text = "Fecha",
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = AppColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                startDate = DateFilter(text = "Desde", context = LocalContext.current, startDate)
                Spacer(modifier = Modifier.height(8.dp))
                endDate = DateFilter(text = "Hasta:", context = LocalContext.current, endDate)
                Spacer(modifier = Modifier.height(32.dp))

                // Filtrado de categorías
                Text(
                    text = "Categoría",
                    fontSize = 20.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = AppColor
                )
                Spacer(modifier = Modifier.height(16.dp))
                selectedCategories = CategoriesFilter(articlesViewModel)

                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Localización", fontSize = 20.sp,
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = AppColor
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Filtrado de localizaciones
                selectedLocations = LocationFilter(articlesViewModel)
                Spacer(modifier = Modifier.height(32.dp))

                // Botones de cancelar y aplicar filtros
                Row() {
                    TextButton(
                        onClick = { onDismissRequest() },
                        Modifier
                            .weight(1f)
                            .padding(end = 16.dp)
                            .border(
                                BorderStroke(1.dp, Color.Black),
                                shape = RoundedCornerShape(4.dp)
                            )
                    ) {
                        Text(
                            text = "Cancelar",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontFamily = poppinsFontFamily
                        )
                    }
                    TextButton(
                        onClick = {
                            Log.d("CATEGORIAS", "{${selectedCategories.joinToString()}}")
                            articlesViewModel.getArticlesByFilter(
                                startDate,
                                endDate,
                                selectedCategories,
                                selectedLocations
                            )
                            onDismissRequest()
                        },
                        colors = ButtonDefaults.textButtonColors(backgroundColor = AppColor),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = "Aplicar",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontFamily = poppinsFontFamily
                        )
                    }
                }


            }
        },
        content = {
            ArticleScreen(navController, articlesViewModel)
        },
        scrimColor = Color.Black.copy(alpha = 0.32f),
        sheetBackgroundColor = MaterialTheme.colors.surface,
        sheetElevation = 16.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),

        )
}

@Composable
fun DateFilter(text: String, context: Context, initialDate: String): String {
    var date by remember { mutableStateOf(initialDate) }
    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            date = "$dayOfMonth/${month + 1}/$year"
        },
        year, month, day,
    )

    Log.d("DatePicker", "Date: ${date}")

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = text, fontSize = 14.sp, fontFamily = poppinsFontFamily)
            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre el Text y el TextField
            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .size(50.dp),
                trailingIcon = {
                    IconButton(onClick = { datePickerDialog.show() }) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = "Icono de calendario"
                        )
                    }
                },
                maxLines = 1,
            )
        }
    }
    return date
}

@Composable
fun CategoriesFilter(viewModel: ArticlesViewModel): List<String> {
    val articles by viewModel.articles.collectAsState()

    var categories by rememberSaveable {
        mutableStateOf(listOf<String>())
    }

    articles.forEach { article ->
        if (!categories.contains(article.category)) {
            categories = categories + article.category
        }
    }

    var selectedCategories by rememberSaveable { mutableStateOf(listOf<String>()) }
    val selectedStates = rememberSaveable { mutableMapOf<String, MutableState<Boolean>>() }

    categories.forEach { category ->
        selectedStates[category] = rememberSaveable { mutableStateOf(false) }
    }

    FlowRow {
        categories.forEach { item ->
            val selected = selectedStates[item] ?: rememberSaveable { mutableStateOf(false) }

            OutlinedButton(
                onClick = {
                    selected.value = !selected.value
                    if (selectedCategories.contains(item)) selectedCategories =
                        selectedCategories - item
                    else selectedCategories = selectedCategories + item
                }, colors = ButtonDefaults.buttonColors(
                    contentColor = if (selected.value) Color.White else Color.Black,
                    backgroundColor = if (selected.value) AppColor else Color.White
                )
            ) {
                Text(text = item)
                if (selected.value) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(15.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre los botones
        }
    }
    return selectedCategories
}

@Composable
fun LocationFilter(viewModel: ArticlesViewModel): List<String> {
    val articles by viewModel.articles.collectAsState()

    var locations by rememberSaveable {
        mutableStateOf(listOf<String>())
    }

    articles.forEach { article ->
        if (!locations.contains(article.location)) {
            locations = locations + article.location
        }
    }

    var selectedLocations by rememberSaveable { mutableStateOf(listOf<String>()) }
    val selectedStates = rememberSaveable { mutableMapOf<String, MutableState<Boolean>>() }

    locations.forEach { location ->
        selectedStates[location] = rememberSaveable { mutableStateOf(false) }


    }

    FlowRow {
        locations.forEach { item ->
            selectedStates[item] = rememberSaveable { mutableStateOf(false) }

            val selected =
                selectedStates[item] ?: rememberSaveable { mutableStateOf(false) }

            OutlinedButton(
                onClick = {
                    selected.value = !selected.value
                    if (selectedLocations.contains(item)) selectedLocations =
                        selectedLocations - item
                    else selectedLocations = selectedLocations + item
                }, colors = ButtonDefaults.buttonColors(
                    contentColor = if (selected.value) Color.White else Color.Black,
                    backgroundColor = if (selected.value) AppColor else Color.White
                )
            ) {
                Text(text = item)
                if (selected.value) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "",
                        tint = Color.White,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(15.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp)) // Espacio entre los botones
        }
    }

    return selectedLocations
}

