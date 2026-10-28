package cl.duocuc.miregistro.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import cl.duocuc.miregistro.data.ProtocolosDbHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProtocolosScreen(onVolver: () -> Unit) {
    val context = LocalContext.current
    val db = remember { ProtocolosDbHelper(context) }
    var protocolos by remember { mutableStateOf(db.listar()) }
    var seccion by remember { mutableStateOf("Protocolos") }
    var mostrarFormulario by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    var busqueda by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("PT-2026-0403") }
    var tag by remember { mutableStateOf("TR-01") }
    var cliente by remember { mutableStateOf("Envasadora del Pacífico S.A.") }
    var instalacion by remember { mutableStateOf("Planta Lampa") }
    var tipo by remember { mutableStateOf("Transformador") }
    var tecnico by remember { mutableStateOf("Matías Godoy Herrera") }
    var fecha by remember { mutableStateOf("27-10-2026") }
    var temperatura by remember { mutableStateOf("20") }
    var humedad by remember { mutableStateOf("50") }
    var lectura1 by remember { mutableStateOf("10000") }
    var lectura10 by remember { mutableStateOf("22000") }
    var comentario by remember { mutableStateOf("") }
    val equipos = listOf("TR-01 · Transformador · Planta Lampa", "TR-02 · Transformador · Planta Lampa", "TR-A · Transformador · Planta Elevadora Sur", "CEL-MT-03 · Celda MT · Edificio Clínico", "INT-52-1 · Interruptor · Faena Til Til", "REL-50/51-A · Relé de protección · Planta Molienda", "CAB-MT-02 · Cable de poder · Site Huechuraba", "TR-SS-01 · Transformador · Faena Til Til")
    val clientes = listOf("Envasadora del Pacífico S.A.", "Sanitaria Valle Central S.A.", "Minera Cerro Alto SpA", "Clínica Los Aromos Ltda.", "Cementos Lomas Blancas S.A.", "Data Center Andes Norte SpA")

    Scaffold(topBar = { TopAppBar(title = { Text("Induztek | Protocolos") }, navigationIcon = { TextButton(onClick = onVolver) { Text("Volver") } }) }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Text("Registro de pruebas eléctricas en terreno", style = MaterialTheme.typography.titleMedium)
            Text("Los registros se guardan en el dispositivo y están disponibles sin conexión.", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Protocolos", "Clientes", "Equipos").forEach { item ->
                    FilterChip(selected = seccion == item, onClick = { seccion = item }, label = { Text(item) })
                }
            }
            Spacer(Modifier.height(8.dp))
            if (seccion == "Protocolos") {
                OutlinedTextField(busqueda, { busqueda = it }, modifier = Modifier.fillMaxWidth(), label = { Text("Buscar por protocolo, TAG o cliente") }, singleLine = true)
                Spacer(Modifier.height(8.dp))
                Button(onClick = { mostrarFormulario = !mostrarFormulario }, modifier = Modifier.fillMaxWidth()) { Text(if (mostrarFormulario) "Cancelar registro" else "+ Registrar protocolo") }
                if (mostrarFormulario) {
                    LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        item {
                            OutlinedTextField(numero, { numero = it }, label = { Text("Número de protocolo") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(tag, { tag = it }, label = { Text("TAG del equipo") }, modifier = Modifier.fillMaxWidth())
                            SelectorSimple("Cliente", cliente, clientes) { cliente = it }
                            OutlinedTextField(instalacion, { instalacion = it }, label = { Text("Instalación") }, modifier = Modifier.fillMaxWidth())
                            SelectorSimple("Tipo de equipo", tipo, listOf("Transformador", "Interruptor", "Celda MT", "Cable de poder", "Relé de protección")) { tipo = it }
                            OutlinedTextField(tecnico, { tecnico = it }, label = { Text("Técnico responsable") }, modifier = Modifier.fillMaxWidth())
                            OutlinedTextField(fecha, { fecha = it }, label = { Text("Fecha (dd-mm-aaaa)") }, modifier = Modifier.fillMaxWidth())
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(temperatura, { temperatura = it }, label = { Text("Temperatura °C") }, modifier = Modifier.weight(1f))
                                OutlinedTextField(humedad, { humedad = it }, label = { Text("Humedad %") }, modifier = Modifier.weight(1f))
                            }
                            Text("Prueba de resistencia de aislación (MΩ)", style = MaterialTheme.typography.titleSmall)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(lectura1, { lectura1 = it }, label = { Text("Lectura 1 min") }, modifier = Modifier.weight(1f))
                                OutlinedTextField(lectura10, { lectura10 = it }, label = { Text("Lectura 10 min") }, modifier = Modifier.weight(1f))
                            }
                            OutlinedTextField(comentario, { comentario = it }, label = { Text("Observaciones") }, modifier = Modifier.fillMaxWidth())
                            Button(onClick = {
                                val ok = db.insertar(numero.trim(), cliente, instalacion, tag.trim(), tipo, tecnico, fecha, temperatura.toDoubleOrNull() ?: -999.0, humedad.toDoubleOrNull() ?: -1.0, lectura1.toDoubleOrNull() ?: -1.0, lectura10.toDoubleOrNull() ?: -1.0, comentario)
                                mensaje = if (ok) "Protocolo guardado. Estado: Registrado en terreno." else "No se guardó. Revisa los valores, rangos y que el número no esté repetido."
                                protocolos = db.listar()
                                if (ok) mostrarFormulario = false
                            }, modifier = Modifier.fillMaxWidth()) { Text("Guardar protocolo") }
                            if (mensaje.isNotBlank()) Text(mensaje, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(protocolos.filter { p -> (p["numero"] + " " + p["tag"] + " " + p["cliente"]).contains(busqueda, true) }) { p ->
                            Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(12.dp)) {
                                Text("${p["numero"]} · ${p["tag"]}", style = MaterialTheme.typography.titleMedium)
                                Text("${p["cliente"]} — ${p["instalacion"]}")
                                Text("${p["fecha"]} · ${p["tipo"]} · ${p["tecnico"]}")
                                Text("Resultado: ${p["resultado"]} | Estado: ${p["estado"]}")
                                Text("Aislación: ${p["lectura1"]} MΩ (1 min) / ${p["lectura10"]} MΩ (10 min)")
                                if (p["comentario"].orEmpty().isNotBlank()) Text("Observación: ${p["comentario"]}")
                                if (p["estado"] != "Formalizado") Text("Pendiente de formalización", color = MaterialTheme.colorScheme.tertiary)
                            } }
                        }
                    }
                }
            } else if (seccion == "Clientes") {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(clientes) { nombre -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(12.dp)) { Text(nombre, style = MaterialTheme.typography.titleMedium); Text("Cliente de ejemplo · Datos ficticios del caso"); Text("Instalaciones: ${when(nombre) { clientes[0] -> "Planta Lampa"; clientes[1] -> "Planta Elevadora Sur"; clientes[2] -> "Faena Til Til"; clientes[3] -> "Edificio Clínico"; clientes[4] -> "Planta Molienda"; else -> "Site Huechuraba" }}") } } } }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(equipos) { equipo -> Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(12.dp)) { Text(equipo, style = MaterialTheme.typography.titleSmall); Text("Registro de equipo del Anexo 9 · historial disponible en Protocolos") } } } }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorSimple(titulo: String, valor: String, opciones: List<String>, alElegir: (String) -> Unit) {
    var abierto by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(expanded = abierto, onExpandedChange = { abierto = !abierto }) {
        OutlinedTextField(value = valor, onValueChange = {}, readOnly = true, label = { Text(titulo) }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = abierto) }, modifier = Modifier.menuAnchor().fillMaxWidth())
        ExposedDropdownMenu(expanded = abierto, onDismissRequest = { abierto = false }) {
            opciones.forEach { opcion -> DropdownMenuItem(text = { Text(opcion) }, onClick = { alElegir(opcion); abierto = false }) }
        }
    }
}
