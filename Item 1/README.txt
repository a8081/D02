
- Parade: status
Consideramos que el status por defecto de la Parade es SUBMITTED ya que interpretamos que al decir que al salvar la parade como final se conserva el status en submitted, quiere decir que anteriormente (en modo draft), estará submitted.

- Segments: GPS
Consideramos las coordenadas GPS con el suficiente poco peso, como para modelarse como un Datatype y no una Entidad independiente.

- Segments: Orden
Consideramos que al ser los Segments de una Parade contiguos, no tiene sentido, de 11 segmentos, modificar el número 6, ya que el resto hasta el total se quedarían descolgados, por lo que consideramos que el único segmento que puede editarse es el Segment final, ya qe el usuario puede decidir ampliar el recorrido o modificar el que acaba de guardar por haberse equivocado. Por lo que podrá editarse o borrarse, dada una lista de x segmentos, solo el segmento x, y al crearse, tendrá que crearse contiguo al último presente en la lista de Segments. 

- Dashboard: Estadisticas sobre el desfile...
Los ratios que se muestran en el recuadro serán del desfile sobre el que hayamos
pulsado: Calcular.

- Administrator Header Menu: Data Breach
Para implementar la normativa de notificar a todos los actores del sistema
de una posible brecha de datos, se incluye en acciones del sistema el boton de
Notificar una brecha de datos. Este envia un mensaje a todos los actores del
sistema menos el administrator que pulsa el boton. Al ser un broadcast, el mensaje
se almacena en la carpeta notification.
