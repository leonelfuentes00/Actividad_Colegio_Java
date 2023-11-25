select Nombre, Apellido, Contrasena, Tipo_Documento, Numero_Documento, Fecha_Nacimiento,
Numero_telefono, Correo_Electronico, Sexo, Nacionalidad, Pais, Provincia, 
Partido, Ciudad, Tipo_Domicilio, Calle, Altura, Piso, 
Departamento, Estado_Afiliacion, Numero_Afiliado, Nombre_Empresa, Cuil_Empresa

from personas 
-- ------------------------------------------------------------------------------------------------------------
inner join nombres_personas on  nombres_personas.FK_ID_Persona= personas.ID_Persona
inner join nombres on nombres_personas.FK_ID_Nombre = nombres.ID_Nombre
-- -------------------------------------------------------------------------------------------------------------
inner join Apellidos_Personas on Apellidos_Personas.FK_ID_Persona = personas.ID_Persona
inner join Apellidos on  Apellidos_personas.FK_ID_Apellido  = Apellidos.ID_Apellido
-- -------------------------------------------------------------------------------------------------------------
inner join documentos_personas on documentos_personas.FK_ID_Persona = personas.ID_Persona
inner join tipos_documentos on documentos_personas.FK_ID_Tipo_Documento = Tipos_Documentos.ID_Tipo_Documento
-- ------------------------------------------------------------------------------------------------------------
inner join Telefonos_personas on telefonos_personas.FK_ID_Persona = personas.ID_Persona
inner join telefonos on  telefonos_personas.FK_ID_Telefono = telefonos.ID_Telefono
-- ------------------------------------------------------------------------------------------------------------
inner join correos_electronicos_personas on correos_electronicos_personas.FK_ID_Persona = personas.ID_Persona
inner join correos_electronicos on  correos_electronicos_personas.FK_ID_Correo_Electronico = correos_electronicos.ID_Correo_Electronico
-- ------------------------------------------------------------------------------------------------------------
inner join sexos_personas on sexos_personas.FK_ID_Persona = personas.ID_Persona
inner join sexos on  sexos_personas.FK_ID_Sexo = sexos.ID_Sexo
-- ------------------------------------------------------------------------------------------------------------
inner join nacionalidades_personas on nacionalidades_personas.FK_ID_Persona = personas.ID_Persona
inner join nacionalidades on nacionalidades_personas.FK_ID_Nacionalidad =  nacionalidades.ID_Nacionalidad
-- ------------------------------------------------------------------------------------------------------------
inner join paises_personas on paises_personas.FK_ID_Persona = personas.ID_Persona
inner join paises on  paises_personas.FK_ID_Pais= paises.ID_Pais
-- ------------------------------------------------------------------------------------------------------------
inner join provincias_personas on provincias_personas.FK_ID_Persona = personas.ID_Persona
inner join provincias on  provincias_personas.FK_ID_Provincia = provincias.ID_Provincia
-- ------------------------------------------------------------------------------------------------------------
inner join partidos_personas on partidos_personas.FK_ID_Persona = personas.ID_Persona
inner join partidos on  partidos_personas.FK_ID_Partido = partidos.ID_Partido
-- ------------------------------------------------------------------------------------------------------------
inner join ciudades_personas on ciudades_personas.FK_ID_Persona = personas.ID_Persona
inner join ciudades on  ciudades_personas.FK_ID_Ciudad = ciudades.ID_Ciudad
-- ------------------------------------------------------------------------------------------------------------
inner join domicilios_personas on domicilios_personas.FK_ID_Persona = personas.ID_Persona 

inner join domicilios_ubicaciones on domicilios_ubicaciones.ID_Domicilio_Ubicacion = domicilios_personas.FK_ID_Domicilio_Ubicacion

inner join calles on calles.ID_Calle = domicilios_ubicaciones.FK_ID_Calle

inner join tipos_domicilios on tipos_domicilios.ID_Tipo_Domicilio = domicilios_ubicaciones.FK_ID_Tipo_Domicilio

-- ------------------------------------------------------------------------------------------------------------
inner join afiliados_personas on afiliados_personas.FK_ID_Persona = personas.ID_Persona
inner join afiliados on  afiliados_personas.FK_ID_Afiliado = afiliados.ID_Afiliado
-- ------------------------------------------------------------------------------------------------------------
  where ID_Persona = 3;
