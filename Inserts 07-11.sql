Select * from Ciudades;
insert into estados_usuarios (Estado_Usuario) values ("En espera"),("Habilitado"),("Denegado");
insert into Usuarios (Usuario) values ("Administrador"),("Docente"),("Alumno"),("Sin Usuario"); 
insert into estados_inscripciones (Estado_Inscripcion) values ("En espera"),("Habilitado"),("Libre"),("Denegado");
insert into Estados_Afiliados (Estado_Afiliado) values ("Si"),("No");
insert into Sexos (Sexo) values ("Masculino"),("Femenino"),("Prefiero no decirlo");
insert into Tipos_Documentos (Tipo_Documento) values ("Dni"),("Pasaporte");



Call Ingresar_Datos_Completos_Persona 
-- Contraseña -- 
("hola", 
-- Nombre, Apellido, Tipo_Documento, Numero_Documento, Fecha Nacimiento --
"Nati", "Perez","DNI", "12345698", "1998-08-20",
-- Numero de Telefono y Correo Electronico --
"2236715241", "hi@gmail.com", 
-- Sexo --
"femenino",
-- Nacionalidad, Pais, Provincia, Partido, Ciudad --
"Argentina", "Argentina", "Buenos Aires","General Pueyrredon", "Mar del Plata", 
-- Tipo domicilio, Calle, Altura, Piso, Departamento (En caso no contar con dos ultimos poner null) --
"Casa", "Colon", 3800, null,null,
-- Estado Afiliado, Numero afiliado, Nombre empresa, Cuil Empresa, 
"Si", 1385, "JAJA", "14888335558",
-- Fecha de Registro --
"2023-11-27");

call Ingresar_Datos_Completos_Institucion(
-- Nombre de la Institucion --
"Caminemos",
-- Ciudad, Calle y Altura --
 "Mar del Plata", "Alvarado", "2350");

call Ingresar_Curso_Completo( 
-- Nombre Docente, Apellido Docente, -- 
"Nicolas", "Garcia",
-- Limite Ausencias, Detalle del Curso, Modulo--  
"25", "Cada Alumno aprendera a programar en Java y a desarrollar una base de datos en MYSQL WORKBENCH", "Aprendiendo a Programar",
-- Nombre Curso, Turno, Hora Desde, Hora Hasta, --
"Programacion", "Mañana", "08:00:00", "12:00:00", 
-- Cupo, Fecha Inicio Curso, Fecha Fin Curso, Nombre Institucion --
"50", "2024-04-03", "2024-11-20", "Caminemos");

call Ingresar_Inscripcion (
-- ID: Persona, Curso, Estado Inscripcion--
"1","1");

call Ingresar_Calificacion (
-- ID Persona, ID Modulo, Calificacion -- 
"1","1","10");

Call Ingresar_Datos_Completos_Persona 
-- Contraseña -- 
("mesias", 
-- Nombre, Apellido, Tipo_Documento, Numero_Documento, Fecha Nacimiento --
"Ian", "Arqueros","DNI", "44957117", "2003-06-29",
-- Numero de Telefono y Correo Electronico --
"2236719263", "ian.arqueros2017@gmail.com", 
-- Sexo --
"Masculino",
-- Nacionalidad, Pais, Provincia, Partido, Ciudad --
"Argentina", "Argentina", "Buenos Aires","General Pueyrredon", "Mar del Plata", 
-- Tipo domicilio, Calle, Altura, Piso, Departamento (En caso no contar con dos ultimos poner null) --
"Casa", "Medrano", 3000, null,null,
-- Estado Afiliado, Numero afiliado, Nombre empresa, Cuil Empresa,
1, 1355, "POPO", "14223335558", 
-- Fecha de Registro --
"2023-10-27");

/*Call Ingresar_Datos_Completos_Persona 
-- Contraseña -- 
("hola", 
-- Nombre, Apellido, Tipo_Documento, Numero_Documento, Fecha Nacimiento --
"Nati", "Perez","DNI", "12345698", "1998-08-20",
-- Numero de Telefono y Correo Electronico --
"2236715241", "hi@gmail.com", 
-- Sexo --
"femenino",
-- Nacionalidad, Pais, Provincia, Partido, Ciudad --
"Argentina", "Argentina", "Buenos Aires","General Pueyrredon", "Mar del Plata", 
-- Tipo domicilio, Calle, Altura, Piso, Departamento (En caso no contar con dos ultimos poner null) --
"Casa", "Colon", 3800, null,null,
-- Estado Afiliado, Numero afiliado, Nombre empresa, Cuil Empresa, Fecha de Registro
1, 1385, "JAJA", "14888335558", "2023-11-27",
-- Tipo De Usuario --
"2");