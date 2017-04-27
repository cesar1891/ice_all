package mx.com.gseguros.portal.cotizacion.model;

import java.lang.reflect.Field;

import mx.com.gseguros.utils.Utils;

public class ConfiguracionCoberturaDTO
{
	private String cdgarant
				   ,otvalor001	,otvalor002	,otvalor003	,otvalor004	,otvalor005	,otvalor006	,otvalor007	,otvalor008	,otvalor009	,otvalor010
				   ,otvalor011	,otvalor012	,otvalor013	,otvalor014	,otvalor015	,otvalor016	,otvalor017	,otvalor018	,otvalor019	,otvalor020
				   ,otvalor021	,otvalor022	,otvalor023	,otvalor024	,otvalor025	,otvalor026	,otvalor027	,otvalor028	,otvalor029	,otvalor030
				   ,otvalor031	,otvalor032	,otvalor033	,otvalor034	,otvalor035	,otvalor036	,otvalor037	,otvalor038	,otvalor039	,otvalor040
				   ,otvalor041	,otvalor042	,otvalor043	,otvalor044	,otvalor045	,otvalor046	,otvalor047	,otvalor048	,otvalor049	,otvalor050
				   ,otvalor051	,otvalor052	,otvalor053	,otvalor054	,otvalor055	,otvalor056	,otvalor057	,otvalor058	,otvalor059	,otvalor060
				   ,otvalor061	,otvalor062	,otvalor063	,otvalor064	,otvalor065	,otvalor066	,otvalor067	,otvalor068	,otvalor069	,otvalor070
				   ,otvalor071	,otvalor072	,otvalor073	,otvalor074	,otvalor075	,otvalor076	,otvalor077	,otvalor078	,otvalor079	,otvalor080
				   ,otvalor081	,otvalor082	,otvalor083	,otvalor084	,otvalor085	,otvalor086	,otvalor087	,otvalor088	,otvalor089	,otvalor090
				   ,otvalor091	,otvalor092	,otvalor093	,otvalor094	,otvalor095	,otvalor096	,otvalor097	,otvalor098	,otvalor099	,otvalor100
				   ,otvalor101	,otvalor102	,otvalor103	,otvalor104	,otvalor105	,otvalor106	,otvalor107	,otvalor108	,otvalor109	,otvalor110
				   ,otvalor111	,otvalor112	,otvalor113	,otvalor114	,otvalor115	,otvalor116	,otvalor117	,otvalor118	,otvalor119	,otvalor120
				   ,otvalor121	,otvalor122	,otvalor123	,otvalor124	,otvalor125	,otvalor126	,otvalor127	,otvalor128	,otvalor129	,otvalor130
				   ,otvalor131	,otvalor132	,otvalor133	,otvalor134	,otvalor135	,otvalor136	,otvalor137	,otvalor138	,otvalor139	,otvalor140
				   ,otvalor141	,otvalor142	,otvalor143	,otvalor144	,otvalor145	,otvalor146	,otvalor147	,otvalor148	,otvalor149	,otvalor150
				   ,otvalor151	,otvalor152	,otvalor153	,otvalor154	,otvalor155	,otvalor156	,otvalor157	,otvalor158	,otvalor159	,otvalor160
				   ,otvalor161	,otvalor162	,otvalor163	,otvalor164	,otvalor165	,otvalor166	,otvalor167	,otvalor168	,otvalor169	,otvalor170
				   ,otvalor171	,otvalor172	,otvalor173	,otvalor174	,otvalor175	,otvalor176	,otvalor177	,otvalor178	,otvalor179	,otvalor180
				   ,otvalor181	,otvalor182	,otvalor183	,otvalor184	,otvalor185	,otvalor186	,otvalor187	,otvalor188	,otvalor189	,otvalor190
				   ,otvalor191	,otvalor192	,otvalor193	,otvalor194	,otvalor195	,otvalor196	,otvalor197	,otvalor198	,otvalor199	,otvalor200
				   ,otvalor201	,otvalor202	,otvalor203	,otvalor204	,otvalor205	,otvalor206	,otvalor207	,otvalor208	,otvalor209	,otvalor210
				   ,otvalor211	,otvalor212	,otvalor213	,otvalor214	,otvalor215	,otvalor216	,otvalor217	,otvalor218	,otvalor219	,otvalor220
				   ,otvalor221	,otvalor222	,otvalor223	,otvalor224	,otvalor225	,otvalor226	,otvalor227	,otvalor228	,otvalor229	,otvalor230
				   ,otvalor231	,otvalor232	,otvalor233	,otvalor234	,otvalor235	,otvalor236	,otvalor237	,otvalor238	,otvalor239	,otvalor240
				   ,otvalor241	,otvalor242	,otvalor243	,otvalor244	,otvalor245	,otvalor246	,otvalor247	,otvalor248	,otvalor249	,otvalor250
				   ,otvalor251	,otvalor252	,otvalor253	,otvalor254	,otvalor255	,otvalor256	,otvalor257	,otvalor258	,otvalor259	,otvalor260
   				   ,otvalor261	,otvalor262	,otvalor263	,otvalor264	,otvalor265	,otvalor266	,otvalor267	,otvalor268	,otvalor269	,otvalor270
				   ,otvalor271	,otvalor272	,otvalor273	,otvalor274	,otvalor275	,otvalor276	,otvalor277	,otvalor278	,otvalor279	,otvalor280
				   ,otvalor281	,otvalor282	,otvalor283	,otvalor284	,otvalor285	,otvalor286	,otvalor287	,otvalor288	,otvalor289	,otvalor290
				   ,otvalor291	,otvalor292	,otvalor293	,otvalor294	,otvalor295	,otvalor296	,otvalor297	,otvalor298	,otvalor299	,otvalor300
				   ,otvalor301	,otvalor302	,otvalor303	,otvalor304	,otvalor305	,otvalor306	,otvalor307	,otvalor308	,otvalor309	,otvalor310
				   ,otvalor311	,otvalor312	,otvalor313	,otvalor314	,otvalor315	,otvalor316	,otvalor317	,otvalor318	,otvalor319	,otvalor320
				   ,otvalor321	,otvalor322	,otvalor323	,otvalor324	,otvalor325	,otvalor326	,otvalor327	,otvalor328	,otvalor329	,otvalor330
				   ,otvalor331	,otvalor332	,otvalor333	,otvalor334	,otvalor335	,otvalor336	,otvalor337	,otvalor338	,otvalor339	,otvalor340
				   ,otvalor341	,otvalor342	,otvalor343	,otvalor344	,otvalor345	,otvalor346	,otvalor347	,otvalor348	,otvalor349	,otvalor350
				   ,otvalor351	,otvalor352	,otvalor353	,otvalor354	,otvalor355	,otvalor356	,otvalor357	,otvalor358	,otvalor359	,otvalor360
				   ,otvalor361	,otvalor362	,otvalor363	,otvalor364	,otvalor365	,otvalor366	,otvalor367	,otvalor368	,otvalor369	,otvalor370
				   ,otvalor371	,otvalor372	,otvalor373	,otvalor374	,otvalor375	,otvalor376	,otvalor377	,otvalor378	,otvalor379	,otvalor380
				   ,otvalor381	,otvalor382	,otvalor383	,otvalor384	,otvalor385	,otvalor386	,otvalor387	,otvalor388	,otvalor389	,otvalor390
				   ,otvalor391	,otvalor392	,otvalor393	,otvalor394	,otvalor395	,otvalor396	,otvalor397	,otvalor398	,otvalor399	,otvalor400
				   ,otvalor401	,otvalor402	,otvalor403	,otvalor404	,otvalor405	,otvalor406	,otvalor407	,otvalor408	,otvalor409	,otvalor410
				   ,otvalor411	,otvalor412	,otvalor413	,otvalor414	,otvalor415	,otvalor416	,otvalor417	,otvalor418	,otvalor419	,otvalor420
				   ,otvalor421	,otvalor422	,otvalor423	,otvalor424	,otvalor425	,otvalor426	,otvalor427	,otvalor428	,otvalor429	,otvalor430
				   ,otvalor431	,otvalor432	,otvalor433	,otvalor434	,otvalor435	,otvalor436	,otvalor437	,otvalor438	,otvalor439	,otvalor440
				   ,otvalor441	,otvalor442	,otvalor443	,otvalor444	,otvalor445	,otvalor446	,otvalor447	,otvalor448	,otvalor449	,otvalor450
				   ,otvalor451	,otvalor452	,otvalor453	,otvalor454	,otvalor455	,otvalor456	,otvalor457	,otvalor458	,otvalor459	,otvalor460
				   ,otvalor461	,otvalor462	,otvalor463	,otvalor464	,otvalor465	,otvalor466	,otvalor467	,otvalor468	,otvalor469	,otvalor470
				   ,otvalor471	,otvalor472	,otvalor473	,otvalor474	,otvalor475	,otvalor476	,otvalor477	,otvalor478	,otvalor479	,otvalor480
				   ,otvalor481	,otvalor482	,otvalor483	,otvalor484	,otvalor485	,otvalor486	,otvalor487	,otvalor488	,otvalor489	,otvalor490
				   ,otvalor491	,otvalor492	,otvalor493	,otvalor494	,otvalor495	,otvalor496	,otvalor497	,otvalor498	,otvalor499	,otvalor500
				   ,otvalor501	,otvalor502	,otvalor503	,otvalor504	,otvalor505	,otvalor506	,otvalor507	,otvalor508	,otvalor509	,otvalor510
				   ,otvalor511	,otvalor512	,otvalor513	,otvalor514	,otvalor515	,otvalor516	,otvalor517	,otvalor518	,otvalor519	,otvalor520
				   ,otvalor521	,otvalor522	,otvalor523	,otvalor524	,otvalor525	,otvalor526	,otvalor527	,otvalor528	,otvalor529	,otvalor530
				   ,otvalor531	,otvalor532	,otvalor533	,otvalor534	,otvalor535	,otvalor536	,otvalor537	,otvalor538	,otvalor539	,otvalor540
				   ,otvalor541	,otvalor542	,otvalor543	,otvalor544	,otvalor545	,otvalor546	,otvalor547	,otvalor548	,otvalor549	,otvalor550
				   ,otvalor551	,otvalor552	,otvalor553	,otvalor554	,otvalor555	,otvalor556	,otvalor557	,otvalor558	,otvalor559	,otvalor560
				   ,otvalor561	,otvalor562	,otvalor563	,otvalor564	,otvalor565	,otvalor566	,otvalor567	,otvalor568	,otvalor569	,otvalor570
				   ,otvalor571	,otvalor572	,otvalor573	,otvalor574	,otvalor575	,otvalor576	,otvalor577	,otvalor578	,otvalor579	,otvalor580
				   ,otvalor581	,otvalor582	,otvalor583	,otvalor584	,otvalor585	,otvalor586	,otvalor587	,otvalor588	,otvalor589	,otvalor590
				   ,otvalor591	,otvalor592	,otvalor593	,otvalor594	,otvalor595	,otvalor596	,otvalor597	,otvalor598	,otvalor599	,otvalor600
				   ,otvalor601	,otvalor602	,otvalor603	,otvalor604	,otvalor605	,otvalor606	,otvalor607	,otvalor608	,otvalor609	,otvalor610
				   ,otvalor611	,otvalor612	,otvalor613	,otvalor614	,otvalor615	,otvalor616	,otvalor617	,otvalor618	,otvalor619	,otvalor620
				   ,otvalor621	,otvalor622	,otvalor623	,otvalor624	,otvalor625	,otvalor626	,otvalor627	,otvalor628	,otvalor629	,otvalor630
				   ,otvalor631	,otvalor632	,otvalor633	,otvalor634	,otvalor635	,otvalor636	,otvalor637	,otvalor638	,otvalor639	,otvalor640;
	
	public ConfiguracionCoberturaDTO(String cdgarant)
	{
		this.cdgarant=cdgarant;
	}
	
	public String[] indexar()
	{
		return new String[]{
				this.cdgarant
				,this.otvalor001	,this.otvalor002	,this.otvalor003	,this.otvalor004	,this.otvalor005	,this.otvalor006	,this.otvalor007	,this.otvalor008	,this.otvalor009	,this.otvalor010
				,this.otvalor011	,this.otvalor012	,this.otvalor013	,this.otvalor014	,this.otvalor015	,this.otvalor016	,this.otvalor017	,this.otvalor018	,this.otvalor019	,this.otvalor020
				,this.otvalor021	,this.otvalor022	,this.otvalor023	,this.otvalor024	,this.otvalor025	,this.otvalor026	,this.otvalor027	,this.otvalor028	,this.otvalor029	,this.otvalor030
				,this.otvalor031	,this.otvalor032	,this.otvalor033	,this.otvalor034	,this.otvalor035	,this.otvalor036	,this.otvalor037	,this.otvalor038	,this.otvalor039	,this.otvalor040
				,this.otvalor041	,this.otvalor042	,this.otvalor043	,this.otvalor044	,this.otvalor045	,this.otvalor046	,this.otvalor047	,this.otvalor048	,this.otvalor049	,this.otvalor050
				,this.otvalor051	,this.otvalor052	,this.otvalor053	,this.otvalor054	,this.otvalor055	,this.otvalor056	,this.otvalor057	,this.otvalor058	,this.otvalor059	,this.otvalor060
				,this.otvalor061	,this.otvalor062	,this.otvalor063	,this.otvalor064	,this.otvalor065	,this.otvalor066	,this.otvalor067	,this.otvalor068	,this.otvalor069	,this.otvalor070
				,this.otvalor071	,this.otvalor072	,this.otvalor073	,this.otvalor074	,this.otvalor075	,this.otvalor076	,this.otvalor077	,this.otvalor078	,this.otvalor079	,this.otvalor080
				,this.otvalor081	,this.otvalor082	,this.otvalor083	,this.otvalor084	,this.otvalor085	,this.otvalor086	,this.otvalor087	,this.otvalor088	,this.otvalor089	,this.otvalor090
				,this.otvalor091	,this.otvalor092	,this.otvalor093	,this.otvalor094	,this.otvalor095	,this.otvalor096	,this.otvalor097	,this.otvalor098	,this.otvalor099	,this.otvalor100
				,this.otvalor101	,this.otvalor102	,this.otvalor103	,this.otvalor104	,this.otvalor105	,this.otvalor106	,this.otvalor107	,this.otvalor108	,this.otvalor109	,this.otvalor110
				,this.otvalor111	,this.otvalor112	,this.otvalor113	,this.otvalor114	,this.otvalor115	,this.otvalor116	,this.otvalor117	,this.otvalor118	,this.otvalor119	,this.otvalor120
				,this.otvalor121	,this.otvalor122	,this.otvalor123	,this.otvalor124	,this.otvalor125	,this.otvalor126	,this.otvalor127	,this.otvalor128	,this.otvalor129	,this.otvalor130
				,this.otvalor131	,this.otvalor132	,this.otvalor133	,this.otvalor134	,this.otvalor135	,this.otvalor136	,this.otvalor137	,this.otvalor138	,this.otvalor139	,this.otvalor140
				,this.otvalor141	,this.otvalor142	,this.otvalor143	,this.otvalor144	,this.otvalor145	,this.otvalor146	,this.otvalor147	,this.otvalor148	,this.otvalor149	,this.otvalor150
				,this.otvalor151	,this.otvalor152	,this.otvalor153	,this.otvalor154	,this.otvalor155	,this.otvalor156	,this.otvalor157	,this.otvalor158	,this.otvalor159	,this.otvalor160
				,this.otvalor161	,this.otvalor162	,this.otvalor163	,this.otvalor164	,this.otvalor165	,this.otvalor166	,this.otvalor167	,this.otvalor168	,this.otvalor169	,this.otvalor170
				,this.otvalor171	,this.otvalor172	,this.otvalor173	,this.otvalor174	,this.otvalor175	,this.otvalor176	,this.otvalor177	,this.otvalor178	,this.otvalor179	,this.otvalor180
				,this.otvalor181	,this.otvalor182	,this.otvalor183	,this.otvalor184	,this.otvalor185	,this.otvalor186	,this.otvalor187	,this.otvalor188	,this.otvalor189	,this.otvalor190
				,this.otvalor191	,this.otvalor192	,this.otvalor193	,this.otvalor194	,this.otvalor195	,this.otvalor196	,this.otvalor197	,this.otvalor198	,this.otvalor199	,this.otvalor200
				,this.otvalor201	,this.otvalor202	,this.otvalor203	,this.otvalor204	,this.otvalor205	,this.otvalor206	,this.otvalor207	,this.otvalor208	,this.otvalor209	,this.otvalor210
				,this.otvalor211	,this.otvalor212	,this.otvalor213	,this.otvalor214	,this.otvalor215	,this.otvalor216	,this.otvalor217	,this.otvalor218	,this.otvalor219	,this.otvalor220
				,this.otvalor221	,this.otvalor222	,this.otvalor223	,this.otvalor224	,this.otvalor225	,this.otvalor226	,this.otvalor227	,this.otvalor228	,this.otvalor229	,this.otvalor230
				,this.otvalor231	,this.otvalor232	,this.otvalor233	,this.otvalor234	,this.otvalor235	,this.otvalor236	,this.otvalor237	,this.otvalor238	,this.otvalor239	,this.otvalor240
				,this.otvalor241	,this.otvalor242	,this.otvalor243	,this.otvalor244	,this.otvalor245	,this.otvalor246	,this.otvalor247	,this.otvalor248	,this.otvalor249	,this.otvalor250
				,this.otvalor251	,this.otvalor252	,this.otvalor253	,this.otvalor254	,this.otvalor255	,this.otvalor256	,this.otvalor257	,this.otvalor258	,this.otvalor259	,this.otvalor260
				,this.otvalor261	,this.otvalor262	,this.otvalor263	,this.otvalor264	,this.otvalor265	,this.otvalor266	,this.otvalor267	,this.otvalor268	,this.otvalor269	,this.otvalor270
				,this.otvalor271	,this.otvalor272	,this.otvalor273	,this.otvalor274	,this.otvalor275	,this.otvalor276	,this.otvalor277	,this.otvalor278	,this.otvalor279	,this.otvalor280
				,this.otvalor281	,this.otvalor282	,this.otvalor283	,this.otvalor284	,this.otvalor285	,this.otvalor286	,this.otvalor287	,this.otvalor288	,this.otvalor289	,this.otvalor290
				,this.otvalor291	,this.otvalor292	,this.otvalor293	,this.otvalor294	,this.otvalor295	,this.otvalor296	,this.otvalor297	,this.otvalor298	,this.otvalor299	,this.otvalor300
				,this.otvalor301	,this.otvalor302	,this.otvalor303	,this.otvalor304	,this.otvalor305	,this.otvalor306	,this.otvalor307	,this.otvalor308	,this.otvalor309	,this.otvalor310
				,this.otvalor311	,this.otvalor312	,this.otvalor313	,this.otvalor314	,this.otvalor315	,this.otvalor316	,this.otvalor317	,this.otvalor318	,this.otvalor319	,this.otvalor320
				,this.otvalor321	,this.otvalor322	,this.otvalor323	,this.otvalor324	,this.otvalor325	,this.otvalor326	,this.otvalor327	,this.otvalor328	,this.otvalor329	,this.otvalor330
				,this.otvalor331	,this.otvalor332	,this.otvalor333	,this.otvalor334	,this.otvalor335	,this.otvalor336	,this.otvalor337	,this.otvalor338	,this.otvalor339	,this.otvalor340
				,this.otvalor341	,this.otvalor342	,this.otvalor343	,this.otvalor344	,this.otvalor345	,this.otvalor346	,this.otvalor347	,this.otvalor348	,this.otvalor349	,this.otvalor350
				,this.otvalor351	,this.otvalor352	,this.otvalor353	,this.otvalor354	,this.otvalor355	,this.otvalor356	,this.otvalor357	,this.otvalor358	,this.otvalor359	,this.otvalor360
				,this.otvalor361	,this.otvalor362	,this.otvalor363	,this.otvalor364	,this.otvalor365	,this.otvalor366	,this.otvalor367	,this.otvalor368	,this.otvalor369	,this.otvalor370
				,this.otvalor371	,this.otvalor372	,this.otvalor373	,this.otvalor374	,this.otvalor375	,this.otvalor376	,this.otvalor377	,this.otvalor378	,this.otvalor379	,this.otvalor380
				,this.otvalor381	,this.otvalor382	,this.otvalor383	,this.otvalor384	,this.otvalor385	,this.otvalor386	,this.otvalor387	,this.otvalor388	,this.otvalor389	,this.otvalor390
				,this.otvalor391	,this.otvalor392	,this.otvalor393	,this.otvalor394	,this.otvalor395	,this.otvalor396	,this.otvalor397	,this.otvalor398	,this.otvalor399	,this.otvalor400
				,this.otvalor401	,this.otvalor402	,this.otvalor403	,this.otvalor404	,this.otvalor405	,this.otvalor406	,this.otvalor407	,this.otvalor408	,this.otvalor409	,this.otvalor410
				,this.otvalor411	,this.otvalor412	,this.otvalor413	,this.otvalor414	,this.otvalor415	,this.otvalor416	,this.otvalor417	,this.otvalor418	,this.otvalor419	,this.otvalor420
				,this.otvalor421	,this.otvalor422	,this.otvalor423	,this.otvalor424	,this.otvalor425	,this.otvalor426	,this.otvalor427	,this.otvalor428	,this.otvalor429	,this.otvalor430
				,this.otvalor431	,this.otvalor432	,this.otvalor433	,this.otvalor434	,this.otvalor435	,this.otvalor436	,this.otvalor437	,this.otvalor438	,this.otvalor439	,this.otvalor440
				,this.otvalor441	,this.otvalor442	,this.otvalor443	,this.otvalor444	,this.otvalor445	,this.otvalor446	,this.otvalor447	,this.otvalor448	,this.otvalor449	,this.otvalor450
				,this.otvalor451	,this.otvalor452	,this.otvalor453	,this.otvalor454	,this.otvalor455	,this.otvalor456	,this.otvalor457	,this.otvalor458	,this.otvalor459	,this.otvalor460
				,this.otvalor461	,this.otvalor462	,this.otvalor463	,this.otvalor464	,this.otvalor465	,this.otvalor466	,this.otvalor467	,this.otvalor468	,this.otvalor469	,this.otvalor470
				,this.otvalor471	,this.otvalor472	,this.otvalor473	,this.otvalor474	,this.otvalor475	,this.otvalor476	,this.otvalor477	,this.otvalor478	,this.otvalor479	,this.otvalor480
				,this.otvalor481	,this.otvalor482	,this.otvalor483	,this.otvalor484	,this.otvalor485	,this.otvalor486	,this.otvalor487	,this.otvalor488	,this.otvalor489	,this.otvalor490
				,this.otvalor491	,this.otvalor492	,this.otvalor493	,this.otvalor494	,this.otvalor495	,this.otvalor496	,this.otvalor497	,this.otvalor498	,this.otvalor499	,this.otvalor500
				,this.otvalor501	,this.otvalor502	,this.otvalor503	,this.otvalor504	,this.otvalor505	,this.otvalor506	,this.otvalor507	,this.otvalor508	,this.otvalor509	,this.otvalor510
				,this.otvalor511	,this.otvalor512	,this.otvalor513	,this.otvalor514	,this.otvalor515	,this.otvalor516	,this.otvalor517	,this.otvalor518	,this.otvalor519	,this.otvalor520
				,this.otvalor521	,this.otvalor522	,this.otvalor523	,this.otvalor524	,this.otvalor525	,this.otvalor526	,this.otvalor527	,this.otvalor528	,this.otvalor529	,this.otvalor530
				,this.otvalor531	,this.otvalor532	,this.otvalor533	,this.otvalor534	,this.otvalor535	,this.otvalor536	,this.otvalor537	,this.otvalor538	,this.otvalor539	,this.otvalor540
				,this.otvalor541	,this.otvalor542	,this.otvalor543	,this.otvalor544	,this.otvalor545	,this.otvalor546	,this.otvalor547	,this.otvalor548	,this.otvalor549	,this.otvalor550
				,this.otvalor551	,this.otvalor552	,this.otvalor553	,this.otvalor554	,this.otvalor555	,this.otvalor556	,this.otvalor557	,this.otvalor558	,this.otvalor559	,this.otvalor560
				,this.otvalor561	,this.otvalor562	,this.otvalor563	,this.otvalor564	,this.otvalor565	,this.otvalor566	,this.otvalor567	,this.otvalor568	,this.otvalor569	,this.otvalor570
				,this.otvalor571	,this.otvalor572	,this.otvalor573	,this.otvalor574	,this.otvalor575	,this.otvalor576	,this.otvalor577	,this.otvalor578	,this.otvalor579	,this.otvalor580
				,this.otvalor581	,this.otvalor582	,this.otvalor583	,this.otvalor584	,this.otvalor585	,this.otvalor586	,this.otvalor587	,this.otvalor588	,this.otvalor589	,this.otvalor590
				,this.otvalor591	,this.otvalor592	,this.otvalor593	,this.otvalor594	,this.otvalor595	,this.otvalor596	,this.otvalor597	,this.otvalor598	,this.otvalor599	,this.otvalor600
				,this.otvalor601	,this.otvalor602	,this.otvalor603	,this.otvalor604	,this.otvalor605	,this.otvalor606	,this.otvalor607	,this.otvalor608	,this.otvalor609	,this.otvalor610
				,this.otvalor611	,this.otvalor612	,this.otvalor613	,this.otvalor614	,this.otvalor615	,this.otvalor616	,this.otvalor617	,this.otvalor618	,this.otvalor619	,this.otvalor620
				,this.otvalor621	,this.otvalor622	,this.otvalor623	,this.otvalor624	,this.otvalor625	,this.otvalor626	,this.otvalor627	,this.otvalor628	,this.otvalor629	,this.otvalor630
				,this.otvalor631	,this.otvalor632	,this.otvalor633	,this.otvalor634	,this.otvalor635	,this.otvalor636	,this.otvalor637	,this.otvalor638	,this.otvalor639	,this.otvalor640
		};
	}
	
	public static String obtenerLlaveAtributos()
	{
		StringBuilder sb = new StringBuilder();
		Field[] fields = ConfiguracionCoberturaDTO.class.getDeclaredFields();
		for(int i=0;i<fields.length;i++)
		{
			sb.append(Utils.join(i+1,"=",fields[i].getName(),","));
		}
		return sb.toString();
	}
	
	public static void main(String[] args)
	{
		System.out.println(ConfiguracionCoberturaDTO.obtenerLlaveAtributos());
	}

	public String getCdgarant() {
		return cdgarant;
	}

	public void setCdgarant(String cdgarant) {
		this.cdgarant = cdgarant;
	}

	public String getOtvalor001() {
		return otvalor001;
	}

	public void setOtvalor001(String otvalor001) {
		this.otvalor001 = otvalor001;
	}

	public String getOtvalor002() {
		return otvalor002;
	}

	public void setOtvalor002(String otvalor002) {
		this.otvalor002 = otvalor002;
	}

	public String getOtvalor003() {
		return otvalor003;
	}

	public void setOtvalor003(String otvalor003) {
		this.otvalor003 = otvalor003;
	}

	public String getOtvalor004() {
		return otvalor004;
	}

	public void setOtvalor004(String otvalor004) {
		this.otvalor004 = otvalor004;
	}

	public String getOtvalor005() {
		return otvalor005;
	}

	public void setOtvalor005(String otvalor005) {
		this.otvalor005 = otvalor005;
	}

	public String getOtvalor006() {
		return otvalor006;
	}

	public void setOtvalor006(String otvalor006) {
		this.otvalor006 = otvalor006;
	}

	public String getOtvalor007() {
		return otvalor007;
	}

	public void setOtvalor007(String otvalor007) {
		this.otvalor007 = otvalor007;
	}

	public String getOtvalor008() {
		return otvalor008;
	}

	public void setOtvalor008(String otvalor008) {
		this.otvalor008 = otvalor008;
	}

	public String getOtvalor009() {
		return otvalor009;
	}

	public void setOtvalor009(String otvalor009) {
		this.otvalor009 = otvalor009;
	}

	public String getOtvalor010() {
		return otvalor010;
	}

	public void setOtvalor010(String otvalor010) {
		this.otvalor010 = otvalor010;
	}

	public String getOtvalor011() {
		return otvalor011;
	}

	public void setOtvalor011(String otvalor011) {
		this.otvalor011 = otvalor011;
	}

	public String getOtvalor012() {
		return otvalor012;
	}

	public void setOtvalor012(String otvalor012) {
		this.otvalor012 = otvalor012;
	}

	public String getOtvalor013() {
		return otvalor013;
	}

	public void setOtvalor013(String otvalor013) {
		this.otvalor013 = otvalor013;
	}

	public String getOtvalor014() {
		return otvalor014;
	}

	public void setOtvalor014(String otvalor014) {
		this.otvalor014 = otvalor014;
	}

	public String getOtvalor015() {
		return otvalor015;
	}

	public void setOtvalor015(String otvalor015) {
		this.otvalor015 = otvalor015;
	}

	public String getOtvalor016() {
		return otvalor016;
	}

	public void setOtvalor016(String otvalor016) {
		this.otvalor016 = otvalor016;
	}

	public String getOtvalor017() {
		return otvalor017;
	}

	public void setOtvalor017(String otvalor017) {
		this.otvalor017 = otvalor017;
	}

	public String getOtvalor018() {
		return otvalor018;
	}

	public void setOtvalor018(String otvalor018) {
		this.otvalor018 = otvalor018;
	}

	public String getOtvalor019() {
		return otvalor019;
	}

	public void setOtvalor019(String otvalor019) {
		this.otvalor019 = otvalor019;
	}

	public String getOtvalor020() {
		return otvalor020;
	}

	public void setOtvalor020(String otvalor020) {
		this.otvalor020 = otvalor020;
	}

	public String getOtvalor021() {
		return otvalor021;
	}

	public void setOtvalor021(String otvalor021) {
		this.otvalor021 = otvalor021;
	}

	public String getOtvalor022() {
		return otvalor022;
	}

	public void setOtvalor022(String otvalor022) {
		this.otvalor022 = otvalor022;
	}

	public String getOtvalor023() {
		return otvalor023;
	}

	public void setOtvalor023(String otvalor023) {
		this.otvalor023 = otvalor023;
	}

	public String getOtvalor024() {
		return otvalor024;
	}

	public void setOtvalor024(String otvalor024) {
		this.otvalor024 = otvalor024;
	}

	public String getOtvalor025() {
		return otvalor025;
	}

	public void setOtvalor025(String otvalor025) {
		this.otvalor025 = otvalor025;
	}

	public String getOtvalor026() {
		return otvalor026;
	}

	public void setOtvalor026(String otvalor026) {
		this.otvalor026 = otvalor026;
	}

	public String getOtvalor027() {
		return otvalor027;
	}

	public void setOtvalor027(String otvalor027) {
		this.otvalor027 = otvalor027;
	}

	public String getOtvalor028() {
		return otvalor028;
	}

	public void setOtvalor028(String otvalor028) {
		this.otvalor028 = otvalor028;
	}

	public String getOtvalor029() {
		return otvalor029;
	}

	public void setOtvalor029(String otvalor029) {
		this.otvalor029 = otvalor029;
	}

	public String getOtvalor030() {
		return otvalor030;
	}

	public void setOtvalor030(String otvalor030) {
		this.otvalor030 = otvalor030;
	}

	public String getOtvalor031() {
		return otvalor031;
	}

	public void setOtvalor031(String otvalor031) {
		this.otvalor031 = otvalor031;
	}

	public String getOtvalor032() {
		return otvalor032;
	}

	public void setOtvalor032(String otvalor032) {
		this.otvalor032 = otvalor032;
	}

	public String getOtvalor033() {
		return otvalor033;
	}

	public void setOtvalor033(String otvalor033) {
		this.otvalor033 = otvalor033;
	}

	public String getOtvalor034() {
		return otvalor034;
	}

	public void setOtvalor034(String otvalor034) {
		this.otvalor034 = otvalor034;
	}

	public String getOtvalor035() {
		return otvalor035;
	}

	public void setOtvalor035(String otvalor035) {
		this.otvalor035 = otvalor035;
	}

	public String getOtvalor036() {
		return otvalor036;
	}

	public void setOtvalor036(String otvalor036) {
		this.otvalor036 = otvalor036;
	}

	public String getOtvalor037() {
		return otvalor037;
	}

	public void setOtvalor037(String otvalor037) {
		this.otvalor037 = otvalor037;
	}

	public String getOtvalor038() {
		return otvalor038;
	}

	public void setOtvalor038(String otvalor038) {
		this.otvalor038 = otvalor038;
	}

	public String getOtvalor039() {
		return otvalor039;
	}

	public void setOtvalor039(String otvalor039) {
		this.otvalor039 = otvalor039;
	}

	public String getOtvalor040() {
		return otvalor040;
	}

	public void setOtvalor040(String otvalor040) {
		this.otvalor040 = otvalor040;
	}

	public String getOtvalor041() {
		return otvalor041;
	}

	public void setOtvalor041(String otvalor041) {
		this.otvalor041 = otvalor041;
	}

	public String getOtvalor042() {
		return otvalor042;
	}

	public void setOtvalor042(String otvalor042) {
		this.otvalor042 = otvalor042;
	}

	public String getOtvalor043() {
		return otvalor043;
	}

	public void setOtvalor043(String otvalor043) {
		this.otvalor043 = otvalor043;
	}

	public String getOtvalor044() {
		return otvalor044;
	}

	public void setOtvalor044(String otvalor044) {
		this.otvalor044 = otvalor044;
	}

	public String getOtvalor045() {
		return otvalor045;
	}

	public void setOtvalor045(String otvalor045) {
		this.otvalor045 = otvalor045;
	}

	public String getOtvalor046() {
		return otvalor046;
	}

	public void setOtvalor046(String otvalor046) {
		this.otvalor046 = otvalor046;
	}

	public String getOtvalor047() {
		return otvalor047;
	}

	public void setOtvalor047(String otvalor047) {
		this.otvalor047 = otvalor047;
	}

	public String getOtvalor048() {
		return otvalor048;
	}

	public void setOtvalor048(String otvalor048) {
		this.otvalor048 = otvalor048;
	}

	public String getOtvalor049() {
		return otvalor049;
	}

	public void setOtvalor049(String otvalor049) {
		this.otvalor049 = otvalor049;
	}

	public String getOtvalor050() {
		return otvalor050;
	}

	public void setOtvalor050(String otvalor050) {
		this.otvalor050 = otvalor050;
	}

	public String getOtvalor051() {
		return otvalor051;
	}

	public void setOtvalor051(String otvalor051) {
		this.otvalor051 = otvalor051;
	}

	public String getOtvalor052() {
		return otvalor052;
	}

	public void setOtvalor052(String otvalor052) {
		this.otvalor052 = otvalor052;
	}

	public String getOtvalor053() {
		return otvalor053;
	}

	public void setOtvalor053(String otvalor053) {
		this.otvalor053 = otvalor053;
	}

	public String getOtvalor054() {
		return otvalor054;
	}

	public void setOtvalor054(String otvalor054) {
		this.otvalor054 = otvalor054;
	}

	public String getOtvalor055() {
		return otvalor055;
	}

	public void setOtvalor055(String otvalor055) {
		this.otvalor055 = otvalor055;
	}

	public String getOtvalor056() {
		return otvalor056;
	}

	public void setOtvalor056(String otvalor056) {
		this.otvalor056 = otvalor056;
	}

	public String getOtvalor057() {
		return otvalor057;
	}

	public void setOtvalor057(String otvalor057) {
		this.otvalor057 = otvalor057;
	}

	public String getOtvalor058() {
		return otvalor058;
	}

	public void setOtvalor058(String otvalor058) {
		this.otvalor058 = otvalor058;
	}

	public String getOtvalor059() {
		return otvalor059;
	}

	public void setOtvalor059(String otvalor059) {
		this.otvalor059 = otvalor059;
	}

	public String getOtvalor060() {
		return otvalor060;
	}

	public void setOtvalor060(String otvalor060) {
		this.otvalor060 = otvalor060;
	}

	public String getOtvalor061() {
		return otvalor061;
	}

	public void setOtvalor061(String otvalor061) {
		this.otvalor061 = otvalor061;
	}

	public String getOtvalor062() {
		return otvalor062;
	}

	public void setOtvalor062(String otvalor062) {
		this.otvalor062 = otvalor062;
	}

	public String getOtvalor063() {
		return otvalor063;
	}

	public void setOtvalor063(String otvalor063) {
		this.otvalor063 = otvalor063;
	}

	public String getOtvalor064() {
		return otvalor064;
	}

	public void setOtvalor064(String otvalor064) {
		this.otvalor064 = otvalor064;
	}

	public String getOtvalor065() {
		return otvalor065;
	}

	public void setOtvalor065(String otvalor065) {
		this.otvalor065 = otvalor065;
	}

	public String getOtvalor066() {
		return otvalor066;
	}

	public void setOtvalor066(String otvalor066) {
		this.otvalor066 = otvalor066;
	}

	public String getOtvalor067() {
		return otvalor067;
	}

	public void setOtvalor067(String otvalor067) {
		this.otvalor067 = otvalor067;
	}

	public String getOtvalor068() {
		return otvalor068;
	}

	public void setOtvalor068(String otvalor068) {
		this.otvalor068 = otvalor068;
	}

	public String getOtvalor069() {
		return otvalor069;
	}

	public void setOtvalor069(String otvalor069) {
		this.otvalor069 = otvalor069;
	}

	public String getOtvalor070() {
		return otvalor070;
	}

	public void setOtvalor070(String otvalor070) {
		this.otvalor070 = otvalor070;
	}

	public String getOtvalor071() {
		return otvalor071;
	}

	public void setOtvalor071(String otvalor071) {
		this.otvalor071 = otvalor071;
	}

	public String getOtvalor072() {
		return otvalor072;
	}

	public void setOtvalor072(String otvalor072) {
		this.otvalor072 = otvalor072;
	}

	public String getOtvalor073() {
		return otvalor073;
	}

	public void setOtvalor073(String otvalor073) {
		this.otvalor073 = otvalor073;
	}

	public String getOtvalor074() {
		return otvalor074;
	}

	public void setOtvalor074(String otvalor074) {
		this.otvalor074 = otvalor074;
	}

	public String getOtvalor075() {
		return otvalor075;
	}

	public void setOtvalor075(String otvalor075) {
		this.otvalor075 = otvalor075;
	}

	public String getOtvalor076() {
		return otvalor076;
	}

	public void setOtvalor076(String otvalor076) {
		this.otvalor076 = otvalor076;
	}

	public String getOtvalor077() {
		return otvalor077;
	}

	public void setOtvalor077(String otvalor077) {
		this.otvalor077 = otvalor077;
	}

	public String getOtvalor078() {
		return otvalor078;
	}

	public void setOtvalor078(String otvalor078) {
		this.otvalor078 = otvalor078;
	}

	public String getOtvalor079() {
		return otvalor079;
	}

	public void setOtvalor079(String otvalor079) {
		this.otvalor079 = otvalor079;
	}

	public String getOtvalor080() {
		return otvalor080;
	}

	public void setOtvalor080(String otvalor080) {
		this.otvalor080 = otvalor080;
	}

	public String getOtvalor081() {
		return otvalor081;
	}

	public void setOtvalor081(String otvalor081) {
		this.otvalor081 = otvalor081;
	}

	public String getOtvalor082() {
		return otvalor082;
	}

	public void setOtvalor082(String otvalor082) {
		this.otvalor082 = otvalor082;
	}

	public String getOtvalor083() {
		return otvalor083;
	}

	public void setOtvalor083(String otvalor083) {
		this.otvalor083 = otvalor083;
	}

	public String getOtvalor084() {
		return otvalor084;
	}

	public void setOtvalor084(String otvalor084) {
		this.otvalor084 = otvalor084;
	}

	public String getOtvalor085() {
		return otvalor085;
	}

	public void setOtvalor085(String otvalor085) {
		this.otvalor085 = otvalor085;
	}

	public String getOtvalor086() {
		return otvalor086;
	}

	public void setOtvalor086(String otvalor086) {
		this.otvalor086 = otvalor086;
	}

	public String getOtvalor087() {
		return otvalor087;
	}

	public void setOtvalor087(String otvalor087) {
		this.otvalor087 = otvalor087;
	}

	public String getOtvalor088() {
		return otvalor088;
	}

	public void setOtvalor088(String otvalor088) {
		this.otvalor088 = otvalor088;
	}

	public String getOtvalor089() {
		return otvalor089;
	}

	public void setOtvalor089(String otvalor089) {
		this.otvalor089 = otvalor089;
	}

	public String getOtvalor090() {
		return otvalor090;
	}

	public void setOtvalor090(String otvalor090) {
		this.otvalor090 = otvalor090;
	}

	public String getOtvalor091() {
		return otvalor091;
	}

	public void setOtvalor091(String otvalor091) {
		this.otvalor091 = otvalor091;
	}

	public String getOtvalor092() {
		return otvalor092;
	}

	public void setOtvalor092(String otvalor092) {
		this.otvalor092 = otvalor092;
	}

	public String getOtvalor093() {
		return otvalor093;
	}

	public void setOtvalor093(String otvalor093) {
		this.otvalor093 = otvalor093;
	}

	public String getOtvalor094() {
		return otvalor094;
	}

	public void setOtvalor094(String otvalor094) {
		this.otvalor094 = otvalor094;
	}

	public String getOtvalor095() {
		return otvalor095;
	}

	public void setOtvalor095(String otvalor095) {
		this.otvalor095 = otvalor095;
	}

	public String getOtvalor096() {
		return otvalor096;
	}

	public void setOtvalor096(String otvalor096) {
		this.otvalor096 = otvalor096;
	}

	public String getOtvalor097() {
		return otvalor097;
	}

	public void setOtvalor097(String otvalor097) {
		this.otvalor097 = otvalor097;
	}

	public String getOtvalor098() {
		return otvalor098;
	}

	public void setOtvalor098(String otvalor098) {
		this.otvalor098 = otvalor098;
	}

	public String getOtvalor099() {
		return otvalor099;
	}

	public void setOtvalor099(String otvalor099) {
		this.otvalor099 = otvalor099;
	}

	public String getOtvalor100() {
		return otvalor100;
	}

	public void setOtvalor100(String otvalor100) {
		this.otvalor100 = otvalor100;
	}

	public String getOtvalor101() {
		return otvalor101;
	}

	public void setOtvalor101(String otvalor101) {
		this.otvalor101 = otvalor101;
	}

	public String getOtvalor102() {
		return otvalor102;
	}

	public void setOtvalor102(String otvalor102) {
		this.otvalor102 = otvalor102;
	}

	public String getOtvalor103() {
		return otvalor103;
	}

	public void setOtvalor103(String otvalor103) {
		this.otvalor103 = otvalor103;
	}

	public String getOtvalor104() {
		return otvalor104;
	}

	public void setOtvalor104(String otvalor104) {
		this.otvalor104 = otvalor104;
	}

	public String getOtvalor105() {
		return otvalor105;
	}

	public void setOtvalor105(String otvalor105) {
		this.otvalor105 = otvalor105;
	}

	public String getOtvalor106() {
		return otvalor106;
	}

	public void setOtvalor106(String otvalor106) {
		this.otvalor106 = otvalor106;
	}

	public String getOtvalor107() {
		return otvalor107;
	}

	public void setOtvalor107(String otvalor107) {
		this.otvalor107 = otvalor107;
	}

	public String getOtvalor108() {
		return otvalor108;
	}

	public void setOtvalor108(String otvalor108) {
		this.otvalor108 = otvalor108;
	}

	public String getOtvalor109() {
		return otvalor109;
	}

	public void setOtvalor109(String otvalor109) {
		this.otvalor109 = otvalor109;
	}

	public String getOtvalor110() {
		return otvalor110;
	}

	public void setOtvalor110(String otvalor110) {
		this.otvalor110 = otvalor110;
	}

	public String getOtvalor111() {
		return otvalor111;
	}

	public void setOtvalor111(String otvalor111) {
		this.otvalor111 = otvalor111;
	}

	public String getOtvalor112() {
		return otvalor112;
	}

	public void setOtvalor112(String otvalor112) {
		this.otvalor112 = otvalor112;
	}

	public String getOtvalor113() {
		return otvalor113;
	}

	public void setOtvalor113(String otvalor113) {
		this.otvalor113 = otvalor113;
	}

	public String getOtvalor114() {
		return otvalor114;
	}

	public void setOtvalor114(String otvalor114) {
		this.otvalor114 = otvalor114;
	}

	public String getOtvalor115() {
		return otvalor115;
	}

	public void setOtvalor115(String otvalor115) {
		this.otvalor115 = otvalor115;
	}

	public String getOtvalor116() {
		return otvalor116;
	}

	public void setOtvalor116(String otvalor116) {
		this.otvalor116 = otvalor116;
	}

	public String getOtvalor117() {
		return otvalor117;
	}

	public void setOtvalor117(String otvalor117) {
		this.otvalor117 = otvalor117;
	}

	public String getOtvalor118() {
		return otvalor118;
	}

	public void setOtvalor118(String otvalor118) {
		this.otvalor118 = otvalor118;
	}

	public String getOtvalor119() {
		return otvalor119;
	}

	public void setOtvalor119(String otvalor119) {
		this.otvalor119 = otvalor119;
	}

	public String getOtvalor120() {
		return otvalor120;
	}

	public void setOtvalor120(String otvalor120) {
		this.otvalor120 = otvalor120;
	}

	public String getOtvalor121() {
		return otvalor121;
	}

	public void setOtvalor121(String otvalor121) {
		this.otvalor121 = otvalor121;
	}

	public String getOtvalor122() {
		return otvalor122;
	}

	public void setOtvalor122(String otvalor122) {
		this.otvalor122 = otvalor122;
	}

	public String getOtvalor123() {
		return otvalor123;
	}

	public void setOtvalor123(String otvalor123) {
		this.otvalor123 = otvalor123;
	}

	public String getOtvalor124() {
		return otvalor124;
	}

	public void setOtvalor124(String otvalor124) {
		this.otvalor124 = otvalor124;
	}

	public String getOtvalor125() {
		return otvalor125;
	}

	public void setOtvalor125(String otvalor125) {
		this.otvalor125 = otvalor125;
	}

	public String getOtvalor126() {
		return otvalor126;
	}

	public void setOtvalor126(String otvalor126) {
		this.otvalor126 = otvalor126;
	}

	public String getOtvalor127() {
		return otvalor127;
	}

	public void setOtvalor127(String otvalor127) {
		this.otvalor127 = otvalor127;
	}

	public String getOtvalor128() {
		return otvalor128;
	}

	public void setOtvalor128(String otvalor128) {
		this.otvalor128 = otvalor128;
	}

	public String getOtvalor129() {
		return otvalor129;
	}

	public void setOtvalor129(String otvalor129) {
		this.otvalor129 = otvalor129;
	}

	public String getOtvalor130() {
		return otvalor130;
	}

	public void setOtvalor130(String otvalor130) {
		this.otvalor130 = otvalor130;
	}

	public String getOtvalor131() {
		return otvalor131;
	}

	public void setOtvalor131(String otvalor131) {
		this.otvalor131 = otvalor131;
	}

	public String getOtvalor132() {
		return otvalor132;
	}

	public void setOtvalor132(String otvalor132) {
		this.otvalor132 = otvalor132;
	}

	public String getOtvalor133() {
		return otvalor133;
	}

	public void setOtvalor133(String otvalor133) {
		this.otvalor133 = otvalor133;
	}

	public String getOtvalor134() {
		return otvalor134;
	}

	public void setOtvalor134(String otvalor134) {
		this.otvalor134 = otvalor134;
	}

	public String getOtvalor135() {
		return otvalor135;
	}

	public void setOtvalor135(String otvalor135) {
		this.otvalor135 = otvalor135;
	}

	public String getOtvalor136() {
		return otvalor136;
	}

	public void setOtvalor136(String otvalor136) {
		this.otvalor136 = otvalor136;
	}

	public String getOtvalor137() {
		return otvalor137;
	}

	public void setOtvalor137(String otvalor137) {
		this.otvalor137 = otvalor137;
	}

	public String getOtvalor138() {
		return otvalor138;
	}

	public void setOtvalor138(String otvalor138) {
		this.otvalor138 = otvalor138;
	}

	public String getOtvalor139() {
		return otvalor139;
	}

	public void setOtvalor139(String otvalor139) {
		this.otvalor139 = otvalor139;
	}

	public String getOtvalor140() {
		return otvalor140;
	}

	public void setOtvalor140(String otvalor140) {
		this.otvalor140 = otvalor140;
	}

	public String getOtvalor141() {
		return otvalor141;
	}

	public void setOtvalor141(String otvalor141) {
		this.otvalor141 = otvalor141;
	}

	public String getOtvalor142() {
		return otvalor142;
	}

	public void setOtvalor142(String otvalor142) {
		this.otvalor142 = otvalor142;
	}

	public String getOtvalor143() {
		return otvalor143;
	}

	public void setOtvalor143(String otvalor143) {
		this.otvalor143 = otvalor143;
	}

	public String getOtvalor144() {
		return otvalor144;
	}

	public void setOtvalor144(String otvalor144) {
		this.otvalor144 = otvalor144;
	}

	public String getOtvalor145() {
		return otvalor145;
	}

	public void setOtvalor145(String otvalor145) {
		this.otvalor145 = otvalor145;
	}

	public String getOtvalor146() {
		return otvalor146;
	}

	public void setOtvalor146(String otvalor146) {
		this.otvalor146 = otvalor146;
	}

	public String getOtvalor147() {
		return otvalor147;
	}

	public void setOtvalor147(String otvalor147) {
		this.otvalor147 = otvalor147;
	}

	public String getOtvalor148() {
		return otvalor148;
	}

	public void setOtvalor148(String otvalor148) {
		this.otvalor148 = otvalor148;
	}

	public String getOtvalor149() {
		return otvalor149;
	}

	public void setOtvalor149(String otvalor149) {
		this.otvalor149 = otvalor149;
	}

	public String getOtvalor150() {
		return otvalor150;
	}

	public void setOtvalor150(String otvalor150) {
		this.otvalor150 = otvalor150;
	}

	public String getOtvalor151() {
		return otvalor151;
	}

	public void setOtvalor151(String otvalor151) {
		this.otvalor151 = otvalor151;
	}

	public String getOtvalor152() {
		return otvalor152;
	}

	public void setOtvalor152(String otvalor152) {
		this.otvalor152 = otvalor152;
	}

	public String getOtvalor153() {
		return otvalor153;
	}

	public void setOtvalor153(String otvalor153) {
		this.otvalor153 = otvalor153;
	}

	public String getOtvalor154() {
		return otvalor154;
	}

	public void setOtvalor154(String otvalor154) {
		this.otvalor154 = otvalor154;
	}

	public String getOtvalor155() {
		return otvalor155;
	}

	public void setOtvalor155(String otvalor155) {
		this.otvalor155 = otvalor155;
	}

	public String getOtvalor156() {
		return otvalor156;
	}

	public void setOtvalor156(String otvalor156) {
		this.otvalor156 = otvalor156;
	}

	public String getOtvalor157() {
		return otvalor157;
	}

	public void setOtvalor157(String otvalor157) {
		this.otvalor157 = otvalor157;
	}

	public String getOtvalor158() {
		return otvalor158;
	}

	public void setOtvalor158(String otvalor158) {
		this.otvalor158 = otvalor158;
	}

	public String getOtvalor159() {
		return otvalor159;
	}

	public void setOtvalor159(String otvalor159) {
		this.otvalor159 = otvalor159;
	}

	public String getOtvalor160() {
		return otvalor160;
	}

	public void setOtvalor160(String otvalor160) {
		this.otvalor160 = otvalor160;
	}

	public String getOtvalor161() {
		return otvalor161;
	}

	public void setOtvalor161(String otvalor161) {
		this.otvalor161 = otvalor161;
	}

	public String getOtvalor162() {
		return otvalor162;
	}

	public void setOtvalor162(String otvalor162) {
		this.otvalor162 = otvalor162;
	}

	public String getOtvalor163() {
		return otvalor163;
	}

	public void setOtvalor163(String otvalor163) {
		this.otvalor163 = otvalor163;
	}

	public String getOtvalor164() {
		return otvalor164;
	}

	public void setOtvalor164(String otvalor164) {
		this.otvalor164 = otvalor164;
	}

	public String getOtvalor165() {
		return otvalor165;
	}

	public void setOtvalor165(String otvalor165) {
		this.otvalor165 = otvalor165;
	}

	public String getOtvalor166() {
		return otvalor166;
	}

	public void setOtvalor166(String otvalor166) {
		this.otvalor166 = otvalor166;
	}

	public String getOtvalor167() {
		return otvalor167;
	}

	public void setOtvalor167(String otvalor167) {
		this.otvalor167 = otvalor167;
	}

	public String getOtvalor168() {
		return otvalor168;
	}

	public void setOtvalor168(String otvalor168) {
		this.otvalor168 = otvalor168;
	}

	public String getOtvalor169() {
		return otvalor169;
	}

	public void setOtvalor169(String otvalor169) {
		this.otvalor169 = otvalor169;
	}

	public String getOtvalor170() {
		return otvalor170;
	}

	public void setOtvalor170(String otvalor170) {
		this.otvalor170 = otvalor170;
	}

	public String getOtvalor171() {
		return otvalor171;
	}

	public void setOtvalor171(String otvalor171) {
		this.otvalor171 = otvalor171;
	}

	public String getOtvalor172() {
		return otvalor172;
	}

	public void setOtvalor172(String otvalor172) {
		this.otvalor172 = otvalor172;
	}

	public String getOtvalor173() {
		return otvalor173;
	}

	public void setOtvalor173(String otvalor173) {
		this.otvalor173 = otvalor173;
	}

	public String getOtvalor174() {
		return otvalor174;
	}

	public void setOtvalor174(String otvalor174) {
		this.otvalor174 = otvalor174;
	}

	public String getOtvalor175() {
		return otvalor175;
	}

	public void setOtvalor175(String otvalor175) {
		this.otvalor175 = otvalor175;
	}

	public String getOtvalor176() {
		return otvalor176;
	}

	public void setOtvalor176(String otvalor176) {
		this.otvalor176 = otvalor176;
	}

	public String getOtvalor177() {
		return otvalor177;
	}

	public void setOtvalor177(String otvalor177) {
		this.otvalor177 = otvalor177;
	}

	public String getOtvalor178() {
		return otvalor178;
	}

	public void setOtvalor178(String otvalor178) {
		this.otvalor178 = otvalor178;
	}

	public String getOtvalor179() {
		return otvalor179;
	}

	public void setOtvalor179(String otvalor179) {
		this.otvalor179 = otvalor179;
	}

	public String getOtvalor180() {
		return otvalor180;
	}

	public void setOtvalor180(String otvalor180) {
		this.otvalor180 = otvalor180;
	}

	public String getOtvalor181() {
		return otvalor181;
	}

	public void setOtvalor181(String otvalor181) {
		this.otvalor181 = otvalor181;
	}

	public String getOtvalor182() {
		return otvalor182;
	}

	public void setOtvalor182(String otvalor182) {
		this.otvalor182 = otvalor182;
	}

	public String getOtvalor183() {
		return otvalor183;
	}

	public void setOtvalor183(String otvalor183) {
		this.otvalor183 = otvalor183;
	}

	public String getOtvalor184() {
		return otvalor184;
	}

	public void setOtvalor184(String otvalor184) {
		this.otvalor184 = otvalor184;
	}

	public String getOtvalor185() {
		return otvalor185;
	}

	public void setOtvalor185(String otvalor185) {
		this.otvalor185 = otvalor185;
	}

	public String getOtvalor186() {
		return otvalor186;
	}

	public void setOtvalor186(String otvalor186) {
		this.otvalor186 = otvalor186;
	}

	public String getOtvalor187() {
		return otvalor187;
	}

	public void setOtvalor187(String otvalor187) {
		this.otvalor187 = otvalor187;
	}

	public String getOtvalor188() {
		return otvalor188;
	}

	public void setOtvalor188(String otvalor188) {
		this.otvalor188 = otvalor188;
	}

	public String getOtvalor189() {
		return otvalor189;
	}

	public void setOtvalor189(String otvalor189) {
		this.otvalor189 = otvalor189;
	}

	public String getOtvalor190() {
		return otvalor190;
	}

	public void setOtvalor190(String otvalor190) {
		this.otvalor190 = otvalor190;
	}

	public String getOtvalor191() {
		return otvalor191;
	}

	public void setOtvalor191(String otvalor191) {
		this.otvalor191 = otvalor191;
	}

	public String getOtvalor192() {
		return otvalor192;
	}

	public void setOtvalor192(String otvalor192) {
		this.otvalor192 = otvalor192;
	}

	public String getOtvalor193() {
		return otvalor193;
	}

	public void setOtvalor193(String otvalor193) {
		this.otvalor193 = otvalor193;
	}

	public String getOtvalor194() {
		return otvalor194;
	}

	public void setOtvalor194(String otvalor194) {
		this.otvalor194 = otvalor194;
	}

	public String getOtvalor195() {
		return otvalor195;
	}

	public void setOtvalor195(String otvalor195) {
		this.otvalor195 = otvalor195;
	}

	public String getOtvalor196() {
		return otvalor196;
	}

	public void setOtvalor196(String otvalor196) {
		this.otvalor196 = otvalor196;
	}

	public String getOtvalor197() {
		return otvalor197;
	}

	public void setOtvalor197(String otvalor197) {
		this.otvalor197 = otvalor197;
	}

	public String getOtvalor198() {
		return otvalor198;
	}

	public void setOtvalor198(String otvalor198) {
		this.otvalor198 = otvalor198;
	}

	public String getOtvalor199() {
		return otvalor199;
	}

	public void setOtvalor199(String otvalor199) {
		this.otvalor199 = otvalor199;
	}

	public String getOtvalor200() {
		return otvalor200;
	}

	public void setOtvalor200(String otvalor200) {
		this.otvalor200 = otvalor200;
	}

	public String getOtvalor201() {
		return otvalor201;
	}

	public void setOtvalor201(String otvalor201) {
		this.otvalor201 = otvalor201;
	}

	public String getOtvalor202() {
		return otvalor202;
	}

	public void setOtvalor202(String otvalor202) {
		this.otvalor202 = otvalor202;
	}

	public String getOtvalor203() {
		return otvalor203;
	}

	public void setOtvalor203(String otvalor203) {
		this.otvalor203 = otvalor203;
	}

	public String getOtvalor204() {
		return otvalor204;
	}

	public void setOtvalor204(String otvalor204) {
		this.otvalor204 = otvalor204;
	}

	public String getOtvalor205() {
		return otvalor205;
	}

	public void setOtvalor205(String otvalor205) {
		this.otvalor205 = otvalor205;
	}

	public String getOtvalor206() {
		return otvalor206;
	}

	public void setOtvalor206(String otvalor206) {
		this.otvalor206 = otvalor206;
	}

	public String getOtvalor207() {
		return otvalor207;
	}

	public void setOtvalor207(String otvalor207) {
		this.otvalor207 = otvalor207;
	}

	public String getOtvalor208() {
		return otvalor208;
	}

	public void setOtvalor208(String otvalor208) {
		this.otvalor208 = otvalor208;
	}

	public String getOtvalor209() {
		return otvalor209;
	}

	public void setOtvalor209(String otvalor209) {
		this.otvalor209 = otvalor209;
	}

	public String getOtvalor210() {
		return otvalor210;
	}

	public void setOtvalor210(String otvalor210) {
		this.otvalor210 = otvalor210;
	}

	public String getOtvalor211() {
		return otvalor211;
	}

	public void setOtvalor211(String otvalor211) {
		this.otvalor211 = otvalor211;
	}

	public String getOtvalor212() {
		return otvalor212;
	}

	public void setOtvalor212(String otvalor212) {
		this.otvalor212 = otvalor212;
	}

	public String getOtvalor213() {
		return otvalor213;
	}

	public void setOtvalor213(String otvalor213) {
		this.otvalor213 = otvalor213;
	}

	public String getOtvalor214() {
		return otvalor214;
	}

	public void setOtvalor214(String otvalor214) {
		this.otvalor214 = otvalor214;
	}

	public String getOtvalor215() {
		return otvalor215;
	}

	public void setOtvalor215(String otvalor215) {
		this.otvalor215 = otvalor215;
	}

	public String getOtvalor216() {
		return otvalor216;
	}

	public void setOtvalor216(String otvalor216) {
		this.otvalor216 = otvalor216;
	}

	public String getOtvalor217() {
		return otvalor217;
	}

	public void setOtvalor217(String otvalor217) {
		this.otvalor217 = otvalor217;
	}

	public String getOtvalor218() {
		return otvalor218;
	}

	public void setOtvalor218(String otvalor218) {
		this.otvalor218 = otvalor218;
	}

	public String getOtvalor219() {
		return otvalor219;
	}

	public void setOtvalor219(String otvalor219) {
		this.otvalor219 = otvalor219;
	}

	public String getOtvalor220() {
		return otvalor220;
	}

	public void setOtvalor220(String otvalor220) {
		this.otvalor220 = otvalor220;
	}

	public String getOtvalor221() {
		return otvalor221;
	}

	public void setOtvalor221(String otvalor221) {
		this.otvalor221 = otvalor221;
	}

	public String getOtvalor222() {
		return otvalor222;
	}

	public void setOtvalor222(String otvalor222) {
		this.otvalor222 = otvalor222;
	}

	public String getOtvalor223() {
		return otvalor223;
	}

	public void setOtvalor223(String otvalor223) {
		this.otvalor223 = otvalor223;
	}

	public String getOtvalor224() {
		return otvalor224;
	}

	public void setOtvalor224(String otvalor224) {
		this.otvalor224 = otvalor224;
	}

	public String getOtvalor225() {
		return otvalor225;
	}

	public void setOtvalor225(String otvalor225) {
		this.otvalor225 = otvalor225;
	}

	public String getOtvalor226() {
		return otvalor226;
	}

	public void setOtvalor226(String otvalor226) {
		this.otvalor226 = otvalor226;
	}

	public String getOtvalor227() {
		return otvalor227;
	}

	public void setOtvalor227(String otvalor227) {
		this.otvalor227 = otvalor227;
	}

	public String getOtvalor228() {
		return otvalor228;
	}

	public void setOtvalor228(String otvalor228) {
		this.otvalor228 = otvalor228;
	}

	public String getOtvalor229() {
		return otvalor229;
	}

	public void setOtvalor229(String otvalor229) {
		this.otvalor229 = otvalor229;
	}

	public String getOtvalor230() {
		return otvalor230;
	}

	public void setOtvalor230(String otvalor230) {
		this.otvalor230 = otvalor230;
	}

	public String getOtvalor231() {
		return otvalor231;
	}

	public void setOtvalor231(String otvalor231) {
		this.otvalor231 = otvalor231;
	}

	public String getOtvalor232() {
		return otvalor232;
	}

	public void setOtvalor232(String otvalor232) {
		this.otvalor232 = otvalor232;
	}

	public String getOtvalor233() {
		return otvalor233;
	}

	public void setOtvalor233(String otvalor233) {
		this.otvalor233 = otvalor233;
	}

	public String getOtvalor234() {
		return otvalor234;
	}

	public void setOtvalor234(String otvalor234) {
		this.otvalor234 = otvalor234;
	}

	public String getOtvalor235() {
		return otvalor235;
	}

	public void setOtvalor235(String otvalor235) {
		this.otvalor235 = otvalor235;
	}

	public String getOtvalor236() {
		return otvalor236;
	}

	public void setOtvalor236(String otvalor236) {
		this.otvalor236 = otvalor236;
	}

	public String getOtvalor237() {
		return otvalor237;
	}

	public void setOtvalor237(String otvalor237) {
		this.otvalor237 = otvalor237;
	}

	public String getOtvalor238() {
		return otvalor238;
	}

	public void setOtvalor238(String otvalor238) {
		this.otvalor238 = otvalor238;
	}

	public String getOtvalor239() {
		return otvalor239;
	}

	public void setOtvalor239(String otvalor239) {
		this.otvalor239 = otvalor239;
	}

	public String getOtvalor240() {
		return otvalor240;
	}

	public void setOtvalor240(String otvalor240) {
		this.otvalor240 = otvalor240;
	}

	public String getOtvalor241() {
		return otvalor241;
	}

	public void setOtvalor241(String otvalor241) {
		this.otvalor241 = otvalor241;
	}

	public String getOtvalor242() {
		return otvalor242;
	}

	public void setOtvalor242(String otvalor242) {
		this.otvalor242 = otvalor242;
	}

	public String getOtvalor243() {
		return otvalor243;
	}

	public void setOtvalor243(String otvalor243) {
		this.otvalor243 = otvalor243;
	}

	public String getOtvalor244() {
		return otvalor244;
	}

	public void setOtvalor244(String otvalor244) {
		this.otvalor244 = otvalor244;
	}

	public String getOtvalor245() {
		return otvalor245;
	}

	public void setOtvalor245(String otvalor245) {
		this.otvalor245 = otvalor245;
	}

	public String getOtvalor246() {
		return otvalor246;
	}

	public void setOtvalor246(String otvalor246) {
		this.otvalor246 = otvalor246;
	}

	public String getOtvalor247() {
		return otvalor247;
	}

	public void setOtvalor247(String otvalor247) {
		this.otvalor247 = otvalor247;
	}

	public String getOtvalor248() {
		return otvalor248;
	}

	public void setOtvalor248(String otvalor248) {
		this.otvalor248 = otvalor248;
	}

	public String getOtvalor249() {
		return otvalor249;
	}

	public void setOtvalor249(String otvalor249) {
		this.otvalor249 = otvalor249;
	}

	public String getOtvalor250() {
		return otvalor250;
	}

	public void setOtvalor250(String otvalor250) {
		this.otvalor250 = otvalor250;
	}

	public String getOtvalor251() {
		return otvalor251;
	}

	public void setOtvalor251(String otvalor251) {
		this.otvalor251 = otvalor251;
	}

	public String getOtvalor252() {
		return otvalor252;
	}

	public void setOtvalor252(String otvalor252) {
		this.otvalor252 = otvalor252;
	}

	public String getOtvalor253() {
		return otvalor253;
	}

	public void setOtvalor253(String otvalor253) {
		this.otvalor253 = otvalor253;
	}

	public String getOtvalor254() {
		return otvalor254;
	}

	public void setOtvalor254(String otvalor254) {
		this.otvalor254 = otvalor254;
	}

	public String getOtvalor255() {
		return otvalor255;
	}

	public void setOtvalor255(String otvalor255) {
		this.otvalor255 = otvalor255;
	}

	public String getOtvalor256() {
		return otvalor256;
	}

	public void setOtvalor256(String otvalor256) {
		this.otvalor256 = otvalor256;
	}

	public String getOtvalor257() {
		return otvalor257;
	}

	public void setOtvalor257(String otvalor257) {
		this.otvalor257 = otvalor257;
	}

	public String getOtvalor258() {
		return otvalor258;
	}

	public void setOtvalor258(String otvalor258) {
		this.otvalor258 = otvalor258;
	}

	public String getOtvalor259() {
		return otvalor259;
	}

	public void setOtvalor259(String otvalor259) {
		this.otvalor259 = otvalor259;
	}

	public String getOtvalor260() {
		return otvalor260;
	}

	public void setOtvalor260(String otvalor260) {
		this.otvalor260 = otvalor260;
	}

	public String getOtvalor261() {
		return otvalor261;
	}

	public void setOtvalor261(String otvalor261) {
		this.otvalor261 = otvalor261;
	}

	public String getOtvalor262() {
		return otvalor262;
	}

	public void setOtvalor262(String otvalor262) {
		this.otvalor262 = otvalor262;
	}

	public String getOtvalor263() {
		return otvalor263;
	}

	public void setOtvalor263(String otvalor263) {
		this.otvalor263 = otvalor263;
	}

	public String getOtvalor264() {
		return otvalor264;
	}

	public void setOtvalor264(String otvalor264) {
		this.otvalor264 = otvalor264;
	}

	public String getOtvalor265() {
		return otvalor265;
	}

	public void setOtvalor265(String otvalor265) {
		this.otvalor265 = otvalor265;
	}

	public String getOtvalor266() {
		return otvalor266;
	}

	public void setOtvalor266(String otvalor266) {
		this.otvalor266 = otvalor266;
	}

	public String getOtvalor267() {
		return otvalor267;
	}

	public void setOtvalor267(String otvalor267) {
		this.otvalor267 = otvalor267;
	}

	public String getOtvalor268() {
		return otvalor268;
	}

	public void setOtvalor268(String otvalor268) {
		this.otvalor268 = otvalor268;
	}

	public String getOtvalor269() {
		return otvalor269;
	}

	public void setOtvalor269(String otvalor269) {
		this.otvalor269 = otvalor269;
	}

	public String getOtvalor270() {
		return otvalor270;
	}

	public void setOtvalor270(String otvalor270) {
		this.otvalor270 = otvalor270;
	}

	public String getOtvalor271() {
		return otvalor271;
	}

	public void setOtvalor271(String otvalor271) {
		this.otvalor271 = otvalor271;
	}

	public String getOtvalor272() {
		return otvalor272;
	}

	public void setOtvalor272(String otvalor272) {
		this.otvalor272 = otvalor272;
	}

	public String getOtvalor273() {
		return otvalor273;
	}

	public void setOtvalor273(String otvalor273) {
		this.otvalor273 = otvalor273;
	}

	public String getOtvalor274() {
		return otvalor274;
	}

	public void setOtvalor274(String otvalor274) {
		this.otvalor274 = otvalor274;
	}

	public String getOtvalor275() {
		return otvalor275;
	}

	public void setOtvalor275(String otvalor275) {
		this.otvalor275 = otvalor275;
	}

	public String getOtvalor276() {
		return otvalor276;
	}

	public void setOtvalor276(String otvalor276) {
		this.otvalor276 = otvalor276;
	}

	public String getOtvalor277() {
		return otvalor277;
	}

	public void setOtvalor277(String otvalor277) {
		this.otvalor277 = otvalor277;
	}

	public String getOtvalor278() {
		return otvalor278;
	}

	public void setOtvalor278(String otvalor278) {
		this.otvalor278 = otvalor278;
	}

	public String getOtvalor279() {
		return otvalor279;
	}

	public void setOtvalor279(String otvalor279) {
		this.otvalor279 = otvalor279;
	}

	public String getOtvalor280() {
		return otvalor280;
	}

	public void setOtvalor280(String otvalor280) {
		this.otvalor280 = otvalor280;
	}

	public String getOtvalor281() {
		return otvalor281;
	}

	public void setOtvalor281(String otvalor281) {
		this.otvalor281 = otvalor281;
	}

	public String getOtvalor282() {
		return otvalor282;
	}

	public void setOtvalor282(String otvalor282) {
		this.otvalor282 = otvalor282;
	}

	public String getOtvalor283() {
		return otvalor283;
	}

	public void setOtvalor283(String otvalor283) {
		this.otvalor283 = otvalor283;
	}

	public String getOtvalor284() {
		return otvalor284;
	}

	public void setOtvalor284(String otvalor284) {
		this.otvalor284 = otvalor284;
	}

	public String getOtvalor285() {
		return otvalor285;
	}

	public void setOtvalor285(String otvalor285) {
		this.otvalor285 = otvalor285;
	}

	public String getOtvalor286() {
		return otvalor286;
	}

	public void setOtvalor286(String otvalor286) {
		this.otvalor286 = otvalor286;
	}

	public String getOtvalor287() {
		return otvalor287;
	}

	public void setOtvalor287(String otvalor287) {
		this.otvalor287 = otvalor287;
	}

	public String getOtvalor288() {
		return otvalor288;
	}

	public void setOtvalor288(String otvalor288) {
		this.otvalor288 = otvalor288;
	}

	public String getOtvalor289() {
		return otvalor289;
	}

	public void setOtvalor289(String otvalor289) {
		this.otvalor289 = otvalor289;
	}

	public String getOtvalor290() {
		return otvalor290;
	}

	public void setOtvalor290(String otvalor290) {
		this.otvalor290 = otvalor290;
	}

	public String getOtvalor291() {
		return otvalor291;
	}

	public void setOtvalor291(String otvalor291) {
		this.otvalor291 = otvalor291;
	}

	public String getOtvalor292() {
		return otvalor292;
	}

	public void setOtvalor292(String otvalor292) {
		this.otvalor292 = otvalor292;
	}

	public String getOtvalor293() {
		return otvalor293;
	}

	public void setOtvalor293(String otvalor293) {
		this.otvalor293 = otvalor293;
	}

	public String getOtvalor294() {
		return otvalor294;
	}

	public void setOtvalor294(String otvalor294) {
		this.otvalor294 = otvalor294;
	}

	public String getOtvalor295() {
		return otvalor295;
	}

	public void setOtvalor295(String otvalor295) {
		this.otvalor295 = otvalor295;
	}

	public String getOtvalor296() {
		return otvalor296;
	}

	public void setOtvalor296(String otvalor296) {
		this.otvalor296 = otvalor296;
	}

	public String getOtvalor297() {
		return otvalor297;
	}

	public void setOtvalor297(String otvalor297) {
		this.otvalor297 = otvalor297;
	}

	public String getOtvalor298() {
		return otvalor298;
	}

	public void setOtvalor298(String otvalor298) {
		this.otvalor298 = otvalor298;
	}

	public String getOtvalor299() {
		return otvalor299;
	}

	public void setOtvalor299(String otvalor299) {
		this.otvalor299 = otvalor299;
	}

	public String getOtvalor300() {
		return otvalor300;
	}

	public void setOtvalor300(String otvalor300) {
		this.otvalor300 = otvalor300;
	}

	public String getOtvalor301() {
		return otvalor301;
	}

	public void setOtvalor301(String otvalor301) {
		this.otvalor301 = otvalor301;
	}

	public String getOtvalor302() {
		return otvalor302;
	}

	public void setOtvalor302(String otvalor302) {
		this.otvalor302 = otvalor302;
	}

	public String getOtvalor303() {
		return otvalor303;
	}

	public void setOtvalor303(String otvalor303) {
		this.otvalor303 = otvalor303;
	}

	public String getOtvalor304() {
		return otvalor304;
	}

	public void setOtvalor304(String otvalor304) {
		this.otvalor304 = otvalor304;
	}

	public String getOtvalor305() {
		return otvalor305;
	}

	public void setOtvalor305(String otvalor305) {
		this.otvalor305 = otvalor305;
	}

	public String getOtvalor306() {
		return otvalor306;
	}

	public void setOtvalor306(String otvalor306) {
		this.otvalor306 = otvalor306;
	}

	public String getOtvalor307() {
		return otvalor307;
	}

	public void setOtvalor307(String otvalor307) {
		this.otvalor307 = otvalor307;
	}

	public String getOtvalor308() {
		return otvalor308;
	}

	public void setOtvalor308(String otvalor308) {
		this.otvalor308 = otvalor308;
	}

	public String getOtvalor309() {
		return otvalor309;
	}

	public void setOtvalor309(String otvalor309) {
		this.otvalor309 = otvalor309;
	}

	public String getOtvalor310() {
		return otvalor310;
	}

	public void setOtvalor310(String otvalor310) {
		this.otvalor310 = otvalor310;
	}

	public String getOtvalor311() {
		return otvalor311;
	}

	public void setOtvalor311(String otvalor311) {
		this.otvalor311 = otvalor311;
	}

	public String getOtvalor312() {
		return otvalor312;
	}

	public void setOtvalor312(String otvalor312) {
		this.otvalor312 = otvalor312;
	}

	public String getOtvalor313() {
		return otvalor313;
	}

	public void setOtvalor313(String otvalor313) {
		this.otvalor313 = otvalor313;
	}

	public String getOtvalor314() {
		return otvalor314;
	}

	public void setOtvalor314(String otvalor314) {
		this.otvalor314 = otvalor314;
	}

	public String getOtvalor315() {
		return otvalor315;
	}

	public void setOtvalor315(String otvalor315) {
		this.otvalor315 = otvalor315;
	}

	public String getOtvalor316() {
		return otvalor316;
	}

	public void setOtvalor316(String otvalor316) {
		this.otvalor316 = otvalor316;
	}

	public String getOtvalor317() {
		return otvalor317;
	}

	public void setOtvalor317(String otvalor317) {
		this.otvalor317 = otvalor317;
	}

	public String getOtvalor318() {
		return otvalor318;
	}

	public void setOtvalor318(String otvalor318) {
		this.otvalor318 = otvalor318;
	}

	public String getOtvalor319() {
		return otvalor319;
	}

	public void setOtvalor319(String otvalor319) {
		this.otvalor319 = otvalor319;
	}

	public String getOtvalor320() {
		return otvalor320;
	}

	public void setOtvalor320(String otvalor320) {
		this.otvalor320 = otvalor320;
	}

	public String getOtvalor321() {
		return otvalor321;
	}

	public void setOtvalor321(String otvalor321) {
		this.otvalor321 = otvalor321;
	}

	public String getOtvalor322() {
		return otvalor322;
	}

	public void setOtvalor322(String otvalor322) {
		this.otvalor322 = otvalor322;
	}

	public String getOtvalor323() {
		return otvalor323;
	}

	public void setOtvalor323(String otvalor323) {
		this.otvalor323 = otvalor323;
	}

	public String getOtvalor324() {
		return otvalor324;
	}

	public void setOtvalor324(String otvalor324) {
		this.otvalor324 = otvalor324;
	}

	public String getOtvalor325() {
		return otvalor325;
	}

	public void setOtvalor325(String otvalor325) {
		this.otvalor325 = otvalor325;
	}

	public String getOtvalor326() {
		return otvalor326;
	}

	public void setOtvalor326(String otvalor326) {
		this.otvalor326 = otvalor326;
	}

	public String getOtvalor327() {
		return otvalor327;
	}

	public void setOtvalor327(String otvalor327) {
		this.otvalor327 = otvalor327;
	}

	public String getOtvalor328() {
		return otvalor328;
	}

	public void setOtvalor328(String otvalor328) {
		this.otvalor328 = otvalor328;
	}

	public String getOtvalor329() {
		return otvalor329;
	}

	public void setOtvalor329(String otvalor329) {
		this.otvalor329 = otvalor329;
	}

	public String getOtvalor330() {
		return otvalor330;
	}

	public void setOtvalor330(String otvalor330) {
		this.otvalor330 = otvalor330;
	}

	public String getOtvalor331() {
		return otvalor331;
	}

	public void setOtvalor331(String otvalor331) {
		this.otvalor331 = otvalor331;
	}

	public String getOtvalor332() {
		return otvalor332;
	}

	public void setOtvalor332(String otvalor332) {
		this.otvalor332 = otvalor332;
	}

	public String getOtvalor333() {
		return otvalor333;
	}

	public void setOtvalor333(String otvalor333) {
		this.otvalor333 = otvalor333;
	}

	public String getOtvalor334() {
		return otvalor334;
	}

	public void setOtvalor334(String otvalor334) {
		this.otvalor334 = otvalor334;
	}

	public String getOtvalor335() {
		return otvalor335;
	}

	public void setOtvalor335(String otvalor335) {
		this.otvalor335 = otvalor335;
	}

	public String getOtvalor336() {
		return otvalor336;
	}

	public void setOtvalor336(String otvalor336) {
		this.otvalor336 = otvalor336;
	}

	public String getOtvalor337() {
		return otvalor337;
	}

	public void setOtvalor337(String otvalor337) {
		this.otvalor337 = otvalor337;
	}

	public String getOtvalor338() {
		return otvalor338;
	}

	public void setOtvalor338(String otvalor338) {
		this.otvalor338 = otvalor338;
	}

	public String getOtvalor339() {
		return otvalor339;
	}

	public void setOtvalor339(String otvalor339) {
		this.otvalor339 = otvalor339;
	}

	public String getOtvalor340() {
		return otvalor340;
	}

	public void setOtvalor340(String otvalor340) {
		this.otvalor340 = otvalor340;
	}

	public String getOtvalor341() {
		return otvalor341;
	}

	public void setOtvalor341(String otvalor341) {
		this.otvalor341 = otvalor341;
	}

	public String getOtvalor342() {
		return otvalor342;
	}

	public void setOtvalor342(String otvalor342) {
		this.otvalor342 = otvalor342;
	}

	public String getOtvalor343() {
		return otvalor343;
	}

	public void setOtvalor343(String otvalor343) {
		this.otvalor343 = otvalor343;
	}

	public String getOtvalor344() {
		return otvalor344;
	}

	public void setOtvalor344(String otvalor344) {
		this.otvalor344 = otvalor344;
	}

	public String getOtvalor345() {
		return otvalor345;
	}

	public void setOtvalor345(String otvalor345) {
		this.otvalor345 = otvalor345;
	}

	public String getOtvalor346() {
		return otvalor346;
	}

	public void setOtvalor346(String otvalor346) {
		this.otvalor346 = otvalor346;
	}

	public String getOtvalor347() {
		return otvalor347;
	}

	public void setOtvalor347(String otvalor347) {
		this.otvalor347 = otvalor347;
	}

	public String getOtvalor348() {
		return otvalor348;
	}

	public void setOtvalor348(String otvalor348) {
		this.otvalor348 = otvalor348;
	}

	public String getOtvalor349() {
		return otvalor349;
	}

	public void setOtvalor349(String otvalor349) {
		this.otvalor349 = otvalor349;
	}

	public String getOtvalor350() {
		return otvalor350;
	}

	public void setOtvalor350(String otvalor350) {
		this.otvalor350 = otvalor350;
	}

	public String getOtvalor351() {
		return otvalor351;
	}

	public void setOtvalor351(String otvalor351) {
		this.otvalor351 = otvalor351;
	}

	public String getOtvalor352() {
		return otvalor352;
	}

	public void setOtvalor352(String otvalor352) {
		this.otvalor352 = otvalor352;
	}

	public String getOtvalor353() {
		return otvalor353;
	}

	public void setOtvalor353(String otvalor353) {
		this.otvalor353 = otvalor353;
	}

	public String getOtvalor354() {
		return otvalor354;
	}

	public void setOtvalor354(String otvalor354) {
		this.otvalor354 = otvalor354;
	}

	public String getOtvalor355() {
		return otvalor355;
	}

	public void setOtvalor355(String otvalor355) {
		this.otvalor355 = otvalor355;
	}

	public String getOtvalor356() {
		return otvalor356;
	}

	public void setOtvalor356(String otvalor356) {
		this.otvalor356 = otvalor356;
	}

	public String getOtvalor357() {
		return otvalor357;
	}

	public void setOtvalor357(String otvalor357) {
		this.otvalor357 = otvalor357;
	}

	public String getOtvalor358() {
		return otvalor358;
	}

	public void setOtvalor358(String otvalor358) {
		this.otvalor358 = otvalor358;
	}

	public String getOtvalor359() {
		return otvalor359;
	}

	public void setOtvalor359(String otvalor359) {
		this.otvalor359 = otvalor359;
	}

	public String getOtvalor360() {
		return otvalor360;
	}

	public void setOtvalor360(String otvalor360) {
		this.otvalor360 = otvalor360;
	}

	public String getOtvalor361() {
		return otvalor361;
	}

	public void setOtvalor361(String otvalor361) {
		this.otvalor361 = otvalor361;
	}

	public String getOtvalor362() {
		return otvalor362;
	}

	public void setOtvalor362(String otvalor362) {
		this.otvalor362 = otvalor362;
	}

	public String getOtvalor363() {
		return otvalor363;
	}

	public void setOtvalor363(String otvalor363) {
		this.otvalor363 = otvalor363;
	}

	public String getOtvalor364() {
		return otvalor364;
	}

	public void setOtvalor364(String otvalor364) {
		this.otvalor364 = otvalor364;
	}

	public String getOtvalor365() {
		return otvalor365;
	}

	public void setOtvalor365(String otvalor365) {
		this.otvalor365 = otvalor365;
	}

	public String getOtvalor366() {
		return otvalor366;
	}

	public void setOtvalor366(String otvalor366) {
		this.otvalor366 = otvalor366;
	}

	public String getOtvalor367() {
		return otvalor367;
	}

	public void setOtvalor367(String otvalor367) {
		this.otvalor367 = otvalor367;
	}

	public String getOtvalor368() {
		return otvalor368;
	}

	public void setOtvalor368(String otvalor368) {
		this.otvalor368 = otvalor368;
	}

	public String getOtvalor369() {
		return otvalor369;
	}

	public void setOtvalor369(String otvalor369) {
		this.otvalor369 = otvalor369;
	}

	public String getOtvalor370() {
		return otvalor370;
	}

	public void setOtvalor370(String otvalor370) {
		this.otvalor370 = otvalor370;
	}

	public String getOtvalor371() {
		return otvalor371;
	}

	public void setOtvalor371(String otvalor371) {
		this.otvalor371 = otvalor371;
	}

	public String getOtvalor372() {
		return otvalor372;
	}

	public void setOtvalor372(String otvalor372) {
		this.otvalor372 = otvalor372;
	}

	public String getOtvalor373() {
		return otvalor373;
	}

	public void setOtvalor373(String otvalor373) {
		this.otvalor373 = otvalor373;
	}

	public String getOtvalor374() {
		return otvalor374;
	}

	public void setOtvalor374(String otvalor374) {
		this.otvalor374 = otvalor374;
	}

	public String getOtvalor375() {
		return otvalor375;
	}

	public void setOtvalor375(String otvalor375) {
		this.otvalor375 = otvalor375;
	}

	public String getOtvalor376() {
		return otvalor376;
	}

	public void setOtvalor376(String otvalor376) {
		this.otvalor376 = otvalor376;
	}

	public String getOtvalor377() {
		return otvalor377;
	}

	public void setOtvalor377(String otvalor377) {
		this.otvalor377 = otvalor377;
	}

	public String getOtvalor378() {
		return otvalor378;
	}

	public void setOtvalor378(String otvalor378) {
		this.otvalor378 = otvalor378;
	}

	public String getOtvalor379() {
		return otvalor379;
	}

	public void setOtvalor379(String otvalor379) {
		this.otvalor379 = otvalor379;
	}

	public String getOtvalor380() {
		return otvalor380;
	}

	public void setOtvalor380(String otvalor380) {
		this.otvalor380 = otvalor380;
	}

	public String getOtvalor381() {
		return otvalor381;
	}

	public void setOtvalor381(String otvalor381) {
		this.otvalor381 = otvalor381;
	}

	public String getOtvalor382() {
		return otvalor382;
	}

	public void setOtvalor382(String otvalor382) {
		this.otvalor382 = otvalor382;
	}

	public String getOtvalor383() {
		return otvalor383;
	}

	public void setOtvalor383(String otvalor383) {
		this.otvalor383 = otvalor383;
	}

	public String getOtvalor384() {
		return otvalor384;
	}

	public void setOtvalor384(String otvalor384) {
		this.otvalor384 = otvalor384;
	}

	public String getOtvalor385() {
		return otvalor385;
	}

	public void setOtvalor385(String otvalor385) {
		this.otvalor385 = otvalor385;
	}

	public String getOtvalor386() {
		return otvalor386;
	}

	public void setOtvalor386(String otvalor386) {
		this.otvalor386 = otvalor386;
	}

	public String getOtvalor387() {
		return otvalor387;
	}

	public void setOtvalor387(String otvalor387) {
		this.otvalor387 = otvalor387;
	}

	public String getOtvalor388() {
		return otvalor388;
	}

	public void setOtvalor388(String otvalor388) {
		this.otvalor388 = otvalor388;
	}

	public String getOtvalor389() {
		return otvalor389;
	}

	public void setOtvalor389(String otvalor389) {
		this.otvalor389 = otvalor389;
	}

	public String getOtvalor390() {
		return otvalor390;
	}

	public void setOtvalor390(String otvalor390) {
		this.otvalor390 = otvalor390;
	}

	public String getOtvalor391() {
		return otvalor391;
	}

	public void setOtvalor391(String otvalor391) {
		this.otvalor391 = otvalor391;
	}

	public String getOtvalor392() {
		return otvalor392;
	}

	public void setOtvalor392(String otvalor392) {
		this.otvalor392 = otvalor392;
	}

	public String getOtvalor393() {
		return otvalor393;
	}

	public void setOtvalor393(String otvalor393) {
		this.otvalor393 = otvalor393;
	}

	public String getOtvalor394() {
		return otvalor394;
	}

	public void setOtvalor394(String otvalor394) {
		this.otvalor394 = otvalor394;
	}

	public String getOtvalor395() {
		return otvalor395;
	}

	public void setOtvalor395(String otvalor395) {
		this.otvalor395 = otvalor395;
	}

	public String getOtvalor396() {
		return otvalor396;
	}

	public void setOtvalor396(String otvalor396) {
		this.otvalor396 = otvalor396;
	}

	public String getOtvalor397() {
		return otvalor397;
	}

	public void setOtvalor397(String otvalor397) {
		this.otvalor397 = otvalor397;
	}

	public String getOtvalor398() {
		return otvalor398;
	}

	public void setOtvalor398(String otvalor398) {
		this.otvalor398 = otvalor398;
	}

	public String getOtvalor399() {
		return otvalor399;
	}

	public void setOtvalor399(String otvalor399) {
		this.otvalor399 = otvalor399;
	}

	public String getOtvalor400() {
		return otvalor400;
	}

	public void setOtvalor400(String otvalor400) {
		this.otvalor400 = otvalor400;
	}

	public String getOtvalor401() {
		return otvalor401;
	}

	public void setOtvalor401(String otvalor401) {
		this.otvalor401 = otvalor401;
	}

	public String getOtvalor402() {
		return otvalor402;
	}

	public void setOtvalor402(String otvalor402) {
		this.otvalor402 = otvalor402;
	}

	public String getOtvalor403() {
		return otvalor403;
	}

	public void setOtvalor403(String otvalor403) {
		this.otvalor403 = otvalor403;
	}

	public String getOtvalor404() {
		return otvalor404;
	}

	public void setOtvalor404(String otvalor404) {
		this.otvalor404 = otvalor404;
	}

	public String getOtvalor405() {
		return otvalor405;
	}

	public void setOtvalor405(String otvalor405) {
		this.otvalor405 = otvalor405;
	}

	public String getOtvalor406() {
		return otvalor406;
	}

	public void setOtvalor406(String otvalor406) {
		this.otvalor406 = otvalor406;
	}

	public String getOtvalor407() {
		return otvalor407;
	}

	public void setOtvalor407(String otvalor407) {
		this.otvalor407 = otvalor407;
	}

	public String getOtvalor408() {
		return otvalor408;
	}

	public void setOtvalor408(String otvalor408) {
		this.otvalor408 = otvalor408;
	}

	public String getOtvalor409() {
		return otvalor409;
	}

	public void setOtvalor409(String otvalor409) {
		this.otvalor409 = otvalor409;
	}

	public String getOtvalor410() {
		return otvalor410;
	}

	public void setOtvalor410(String otvalor410) {
		this.otvalor410 = otvalor410;
	}

	public String getOtvalor411() {
		return otvalor411;
	}

	public void setOtvalor411(String otvalor411) {
		this.otvalor411 = otvalor411;
	}

	public String getOtvalor412() {
		return otvalor412;
	}

	public void setOtvalor412(String otvalor412) {
		this.otvalor412 = otvalor412;
	}

	public String getOtvalor413() {
		return otvalor413;
	}

	public void setOtvalor413(String otvalor413) {
		this.otvalor413 = otvalor413;
	}

	public String getOtvalor414() {
		return otvalor414;
	}

	public void setOtvalor414(String otvalor414) {
		this.otvalor414 = otvalor414;
	}

	public String getOtvalor415() {
		return otvalor415;
	}

	public void setOtvalor415(String otvalor415) {
		this.otvalor415 = otvalor415;
	}

	public String getOtvalor416() {
		return otvalor416;
	}

	public void setOtvalor416(String otvalor416) {
		this.otvalor416 = otvalor416;
	}

	public String getOtvalor417() {
		return otvalor417;
	}

	public void setOtvalor417(String otvalor417) {
		this.otvalor417 = otvalor417;
	}

	public String getOtvalor418() {
		return otvalor418;
	}

	public void setOtvalor418(String otvalor418) {
		this.otvalor418 = otvalor418;
	}

	public String getOtvalor419() {
		return otvalor419;
	}

	public void setOtvalor419(String otvalor419) {
		this.otvalor419 = otvalor419;
	}

	public String getOtvalor420() {
		return otvalor420;
	}

	public void setOtvalor420(String otvalor420) {
		this.otvalor420 = otvalor420;
	}

	public String getOtvalor421() {
		return otvalor421;
	}

	public void setOtvalor421(String otvalor421) {
		this.otvalor421 = otvalor421;
	}

	public String getOtvalor422() {
		return otvalor422;
	}

	public void setOtvalor422(String otvalor422) {
		this.otvalor422 = otvalor422;
	}

	public String getOtvalor423() {
		return otvalor423;
	}

	public void setOtvalor423(String otvalor423) {
		this.otvalor423 = otvalor423;
	}

	public String getOtvalor424() {
		return otvalor424;
	}

	public void setOtvalor424(String otvalor424) {
		this.otvalor424 = otvalor424;
	}

	public String getOtvalor425() {
		return otvalor425;
	}

	public void setOtvalor425(String otvalor425) {
		this.otvalor425 = otvalor425;
	}

	public String getOtvalor426() {
		return otvalor426;
	}

	public void setOtvalor426(String otvalor426) {
		this.otvalor426 = otvalor426;
	}

	public String getOtvalor427() {
		return otvalor427;
	}

	public void setOtvalor427(String otvalor427) {
		this.otvalor427 = otvalor427;
	}

	public String getOtvalor428() {
		return otvalor428;
	}

	public void setOtvalor428(String otvalor428) {
		this.otvalor428 = otvalor428;
	}

	public String getOtvalor429() {
		return otvalor429;
	}

	public void setOtvalor429(String otvalor429) {
		this.otvalor429 = otvalor429;
	}

	public String getOtvalor430() {
		return otvalor430;
	}

	public void setOtvalor430(String otvalor430) {
		this.otvalor430 = otvalor430;
	}

	public String getOtvalor431() {
		return otvalor431;
	}

	public void setOtvalor431(String otvalor431) {
		this.otvalor431 = otvalor431;
	}

	public String getOtvalor432() {
		return otvalor432;
	}

	public void setOtvalor432(String otvalor432) {
		this.otvalor432 = otvalor432;
	}

	public String getOtvalor433() {
		return otvalor433;
	}

	public void setOtvalor433(String otvalor433) {
		this.otvalor433 = otvalor433;
	}

	public String getOtvalor434() {
		return otvalor434;
	}

	public void setOtvalor434(String otvalor434) {
		this.otvalor434 = otvalor434;
	}

	public String getOtvalor435() {
		return otvalor435;
	}

	public void setOtvalor435(String otvalor435) {
		this.otvalor435 = otvalor435;
	}

	public String getOtvalor436() {
		return otvalor436;
	}

	public void setOtvalor436(String otvalor436) {
		this.otvalor436 = otvalor436;
	}

	public String getOtvalor437() {
		return otvalor437;
	}

	public void setOtvalor437(String otvalor437) {
		this.otvalor437 = otvalor437;
	}

	public String getOtvalor438() {
		return otvalor438;
	}

	public void setOtvalor438(String otvalor438) {
		this.otvalor438 = otvalor438;
	}

	public String getOtvalor439() {
		return otvalor439;
	}

	public void setOtvalor439(String otvalor439) {
		this.otvalor439 = otvalor439;
	}

	public String getOtvalor440() {
		return otvalor440;
	}

	public void setOtvalor440(String otvalor440) {
		this.otvalor440 = otvalor440;
	}

	public String getOtvalor441() {
		return otvalor441;
	}

	public void setOtvalor441(String otvalor441) {
		this.otvalor441 = otvalor441;
	}

	public String getOtvalor442() {
		return otvalor442;
	}

	public void setOtvalor442(String otvalor442) {
		this.otvalor442 = otvalor442;
	}

	public String getOtvalor443() {
		return otvalor443;
	}

	public void setOtvalor443(String otvalor443) {
		this.otvalor443 = otvalor443;
	}

	public String getOtvalor444() {
		return otvalor444;
	}

	public void setOtvalor444(String otvalor444) {
		this.otvalor444 = otvalor444;
	}

	public String getOtvalor445() {
		return otvalor445;
	}

	public void setOtvalor445(String otvalor445) {
		this.otvalor445 = otvalor445;
	}

	public String getOtvalor446() {
		return otvalor446;
	}

	public void setOtvalor446(String otvalor446) {
		this.otvalor446 = otvalor446;
	}

	public String getOtvalor447() {
		return otvalor447;
	}

	public void setOtvalor447(String otvalor447) {
		this.otvalor447 = otvalor447;
	}

	public String getOtvalor448() {
		return otvalor448;
	}

	public void setOtvalor448(String otvalor448) {
		this.otvalor448 = otvalor448;
	}

	public String getOtvalor449() {
		return otvalor449;
	}

	public void setOtvalor449(String otvalor449) {
		this.otvalor449 = otvalor449;
	}

	public String getOtvalor450() {
		return otvalor450;
	}

	public void setOtvalor450(String otvalor450) {
		this.otvalor450 = otvalor450;
	}

	public String getOtvalor451() {
		return otvalor451;
	}

	public void setOtvalor451(String otvalor451) {
		this.otvalor451 = otvalor451;
	}

	public String getOtvalor452() {
		return otvalor452;
	}

	public void setOtvalor452(String otvalor452) {
		this.otvalor452 = otvalor452;
	}

	public String getOtvalor453() {
		return otvalor453;
	}

	public void setOtvalor453(String otvalor453) {
		this.otvalor453 = otvalor453;
	}

	public String getOtvalor454() {
		return otvalor454;
	}

	public void setOtvalor454(String otvalor454) {
		this.otvalor454 = otvalor454;
	}

	public String getOtvalor455() {
		return otvalor455;
	}

	public void setOtvalor455(String otvalor455) {
		this.otvalor455 = otvalor455;
	}

	public String getOtvalor456() {
		return otvalor456;
	}

	public void setOtvalor456(String otvalor456) {
		this.otvalor456 = otvalor456;
	}

	public String getOtvalor457() {
		return otvalor457;
	}

	public void setOtvalor457(String otvalor457) {
		this.otvalor457 = otvalor457;
	}

	public String getOtvalor458() {
		return otvalor458;
	}

	public void setOtvalor458(String otvalor458) {
		this.otvalor458 = otvalor458;
	}

	public String getOtvalor459() {
		return otvalor459;
	}

	public void setOtvalor459(String otvalor459) {
		this.otvalor459 = otvalor459;
	}

	public String getOtvalor460() {
		return otvalor460;
	}

	public void setOtvalor460(String otvalor460) {
		this.otvalor460 = otvalor460;
	}

	public String getOtvalor461() {
		return otvalor461;
	}

	public void setOtvalor461(String otvalor461) {
		this.otvalor461 = otvalor461;
	}

	public String getOtvalor462() {
		return otvalor462;
	}

	public void setOtvalor462(String otvalor462) {
		this.otvalor462 = otvalor462;
	}

	public String getOtvalor463() {
		return otvalor463;
	}

	public void setOtvalor463(String otvalor463) {
		this.otvalor463 = otvalor463;
	}

	public String getOtvalor464() {
		return otvalor464;
	}

	public void setOtvalor464(String otvalor464) {
		this.otvalor464 = otvalor464;
	}

	public String getOtvalor465() {
		return otvalor465;
	}

	public void setOtvalor465(String otvalor465) {
		this.otvalor465 = otvalor465;
	}

	public String getOtvalor466() {
		return otvalor466;
	}

	public void setOtvalor466(String otvalor466) {
		this.otvalor466 = otvalor466;
	}

	public String getOtvalor467() {
		return otvalor467;
	}

	public void setOtvalor467(String otvalor467) {
		this.otvalor467 = otvalor467;
	}

	public String getOtvalor468() {
		return otvalor468;
	}

	public void setOtvalor468(String otvalor468) {
		this.otvalor468 = otvalor468;
	}

	public String getOtvalor469() {
		return otvalor469;
	}

	public void setOtvalor469(String otvalor469) {
		this.otvalor469 = otvalor469;
	}

	public String getOtvalor470() {
		return otvalor470;
	}

	public void setOtvalor470(String otvalor470) {
		this.otvalor470 = otvalor470;
	}

	public String getOtvalor471() {
		return otvalor471;
	}

	public void setOtvalor471(String otvalor471) {
		this.otvalor471 = otvalor471;
	}

	public String getOtvalor472() {
		return otvalor472;
	}

	public void setOtvalor472(String otvalor472) {
		this.otvalor472 = otvalor472;
	}

	public String getOtvalor473() {
		return otvalor473;
	}

	public void setOtvalor473(String otvalor473) {
		this.otvalor473 = otvalor473;
	}

	public String getOtvalor474() {
		return otvalor474;
	}

	public void setOtvalor474(String otvalor474) {
		this.otvalor474 = otvalor474;
	}

	public String getOtvalor475() {
		return otvalor475;
	}

	public void setOtvalor475(String otvalor475) {
		this.otvalor475 = otvalor475;
	}

	public String getOtvalor476() {
		return otvalor476;
	}

	public void setOtvalor476(String otvalor476) {
		this.otvalor476 = otvalor476;
	}

	public String getOtvalor477() {
		return otvalor477;
	}

	public void setOtvalor477(String otvalor477) {
		this.otvalor477 = otvalor477;
	}

	public String getOtvalor478() {
		return otvalor478;
	}

	public void setOtvalor478(String otvalor478) {
		this.otvalor478 = otvalor478;
	}

	public String getOtvalor479() {
		return otvalor479;
	}

	public void setOtvalor479(String otvalor479) {
		this.otvalor479 = otvalor479;
	}

	public String getOtvalor480() {
		return otvalor480;
	}

	public void setOtvalor480(String otvalor480) {
		this.otvalor480 = otvalor480;
	}

	public String getOtvalor481() {
		return otvalor481;
	}

	public void setOtvalor481(String otvalor481) {
		this.otvalor481 = otvalor481;
	}

	public String getOtvalor482() {
		return otvalor482;
	}

	public void setOtvalor482(String otvalor482) {
		this.otvalor482 = otvalor482;
	}

	public String getOtvalor483() {
		return otvalor483;
	}

	public void setOtvalor483(String otvalor483) {
		this.otvalor483 = otvalor483;
	}

	public String getOtvalor484() {
		return otvalor484;
	}

	public void setOtvalor484(String otvalor484) {
		this.otvalor484 = otvalor484;
	}

	public String getOtvalor485() {
		return otvalor485;
	}

	public void setOtvalor485(String otvalor485) {
		this.otvalor485 = otvalor485;
	}

	public String getOtvalor486() {
		return otvalor486;
	}

	public void setOtvalor486(String otvalor486) {
		this.otvalor486 = otvalor486;
	}

	public String getOtvalor487() {
		return otvalor487;
	}

	public void setOtvalor487(String otvalor487) {
		this.otvalor487 = otvalor487;
	}

	public String getOtvalor488() {
		return otvalor488;
	}

	public void setOtvalor488(String otvalor488) {
		this.otvalor488 = otvalor488;
	}

	public String getOtvalor489() {
		return otvalor489;
	}

	public void setOtvalor489(String otvalor489) {
		this.otvalor489 = otvalor489;
	}

	public String getOtvalor490() {
		return otvalor490;
	}

	public void setOtvalor490(String otvalor490) {
		this.otvalor490 = otvalor490;
	}

	public String getOtvalor491() {
		return otvalor491;
	}

	public void setOtvalor491(String otvalor491) {
		this.otvalor491 = otvalor491;
	}

	public String getOtvalor492() {
		return otvalor492;
	}

	public void setOtvalor492(String otvalor492) {
		this.otvalor492 = otvalor492;
	}

	public String getOtvalor493() {
		return otvalor493;
	}

	public void setOtvalor493(String otvalor493) {
		this.otvalor493 = otvalor493;
	}

	public String getOtvalor494() {
		return otvalor494;
	}

	public void setOtvalor494(String otvalor494) {
		this.otvalor494 = otvalor494;
	}

	public String getOtvalor495() {
		return otvalor495;
	}

	public void setOtvalor495(String otvalor495) {
		this.otvalor495 = otvalor495;
	}

	public String getOtvalor496() {
		return otvalor496;
	}

	public void setOtvalor496(String otvalor496) {
		this.otvalor496 = otvalor496;
	}

	public String getOtvalor497() {
		return otvalor497;
	}

	public void setOtvalor497(String otvalor497) {
		this.otvalor497 = otvalor497;
	}

	public String getOtvalor498() {
		return otvalor498;
	}

	public void setOtvalor498(String otvalor498) {
		this.otvalor498 = otvalor498;
	}

	public String getOtvalor499() {
		return otvalor499;
	}

	public void setOtvalor499(String otvalor499) {
		this.otvalor499 = otvalor499;
	}

	public String getOtvalor500() {
		return otvalor500;
	}

	public void setOtvalor500(String otvalor500) {
		this.otvalor500 = otvalor500;
	}

	public String getOtvalor501() {
		return otvalor501;
	}

	public void setOtvalor501(String otvalor501) {
		this.otvalor501 = otvalor501;
	}

	public String getOtvalor502() {
		return otvalor502;
	}

	public void setOtvalor502(String otvalor502) {
		this.otvalor502 = otvalor502;
	}

	public String getOtvalor503() {
		return otvalor503;
	}

	public void setOtvalor503(String otvalor503) {
		this.otvalor503 = otvalor503;
	}

	public String getOtvalor504() {
		return otvalor504;
	}

	public void setOtvalor504(String otvalor504) {
		this.otvalor504 = otvalor504;
	}

	public String getOtvalor505() {
		return otvalor505;
	}

	public void setOtvalor505(String otvalor505) {
		this.otvalor505 = otvalor505;
	}

	public String getOtvalor506() {
		return otvalor506;
	}

	public void setOtvalor506(String otvalor506) {
		this.otvalor506 = otvalor506;
	}

	public String getOtvalor507() {
		return otvalor507;
	}

	public void setOtvalor507(String otvalor507) {
		this.otvalor507 = otvalor507;
	}

	public String getOtvalor508() {
		return otvalor508;
	}

	public void setOtvalor508(String otvalor508) {
		this.otvalor508 = otvalor508;
	}

	public String getOtvalor509() {
		return otvalor509;
	}

	public void setOtvalor509(String otvalor509) {
		this.otvalor509 = otvalor509;
	}

	public String getOtvalor510() {
		return otvalor510;
	}

	public void setOtvalor510(String otvalor510) {
		this.otvalor510 = otvalor510;
	}

	public String getOtvalor511() {
		return otvalor511;
	}

	public void setOtvalor511(String otvalor511) {
		this.otvalor511 = otvalor511;
	}

	public String getOtvalor512() {
		return otvalor512;
	}

	public void setOtvalor512(String otvalor512) {
		this.otvalor512 = otvalor512;
	}

	public String getOtvalor513() {
		return otvalor513;
	}

	public void setOtvalor513(String otvalor513) {
		this.otvalor513 = otvalor513;
	}

	public String getOtvalor514() {
		return otvalor514;
	}

	public void setOtvalor514(String otvalor514) {
		this.otvalor514 = otvalor514;
	}

	public String getOtvalor515() {
		return otvalor515;
	}

	public void setOtvalor515(String otvalor515) {
		this.otvalor515 = otvalor515;
	}

	public String getOtvalor516() {
		return otvalor516;
	}

	public void setOtvalor516(String otvalor516) {
		this.otvalor516 = otvalor516;
	}

	public String getOtvalor517() {
		return otvalor517;
	}

	public void setOtvalor517(String otvalor517) {
		this.otvalor517 = otvalor517;
	}

	public String getOtvalor518() {
		return otvalor518;
	}

	public void setOtvalor518(String otvalor518) {
		this.otvalor518 = otvalor518;
	}

	public String getOtvalor519() {
		return otvalor519;
	}

	public void setOtvalor519(String otvalor519) {
		this.otvalor519 = otvalor519;
	}

	public String getOtvalor520() {
		return otvalor520;
	}

	public void setOtvalor520(String otvalor520) {
		this.otvalor520 = otvalor520;
	}

	public String getOtvalor521() {
		return otvalor521;
	}

	public void setOtvalor521(String otvalor521) {
		this.otvalor521 = otvalor521;
	}

	public String getOtvalor522() {
		return otvalor522;
	}

	public void setOtvalor522(String otvalor522) {
		this.otvalor522 = otvalor522;
	}

	public String getOtvalor523() {
		return otvalor523;
	}

	public void setOtvalor523(String otvalor523) {
		this.otvalor523 = otvalor523;
	}

	public String getOtvalor524() {
		return otvalor524;
	}

	public void setOtvalor524(String otvalor524) {
		this.otvalor524 = otvalor524;
	}

	public String getOtvalor525() {
		return otvalor525;
	}

	public void setOtvalor525(String otvalor525) {
		this.otvalor525 = otvalor525;
	}

	public String getOtvalor526() {
		return otvalor526;
	}

	public void setOtvalor526(String otvalor526) {
		this.otvalor526 = otvalor526;
	}

	public String getOtvalor527() {
		return otvalor527;
	}

	public void setOtvalor527(String otvalor527) {
		this.otvalor527 = otvalor527;
	}

	public String getOtvalor528() {
		return otvalor528;
	}

	public void setOtvalor528(String otvalor528) {
		this.otvalor528 = otvalor528;
	}

	public String getOtvalor529() {
		return otvalor529;
	}

	public void setOtvalor529(String otvalor529) {
		this.otvalor529 = otvalor529;
	}

	public String getOtvalor530() {
		return otvalor530;
	}

	public void setOtvalor530(String otvalor530) {
		this.otvalor530 = otvalor530;
	}

	public String getOtvalor531() {
		return otvalor531;
	}

	public void setOtvalor531(String otvalor531) {
		this.otvalor531 = otvalor531;
	}

	public String getOtvalor532() {
		return otvalor532;
	}

	public void setOtvalor532(String otvalor532) {
		this.otvalor532 = otvalor532;
	}

	public String getOtvalor533() {
		return otvalor533;
	}

	public void setOtvalor533(String otvalor533) {
		this.otvalor533 = otvalor533;
	}

	public String getOtvalor534() {
		return otvalor534;
	}

	public void setOtvalor534(String otvalor534) {
		this.otvalor534 = otvalor534;
	}

	public String getOtvalor535() {
		return otvalor535;
	}

	public void setOtvalor535(String otvalor535) {
		this.otvalor535 = otvalor535;
	}

	public String getOtvalor536() {
		return otvalor536;
	}

	public void setOtvalor536(String otvalor536) {
		this.otvalor536 = otvalor536;
	}

	public String getOtvalor537() {
		return otvalor537;
	}

	public void setOtvalor537(String otvalor537) {
		this.otvalor537 = otvalor537;
	}

	public String getOtvalor538() {
		return otvalor538;
	}

	public void setOtvalor538(String otvalor538) {
		this.otvalor538 = otvalor538;
	}

	public String getOtvalor539() {
		return otvalor539;
	}

	public void setOtvalor539(String otvalor539) {
		this.otvalor539 = otvalor539;
	}

	public String getOtvalor540() {
		return otvalor540;
	}

	public void setOtvalor540(String otvalor540) {
		this.otvalor540 = otvalor540;
	}

	public String getOtvalor541() {
		return otvalor541;
	}

	public void setOtvalor541(String otvalor541) {
		this.otvalor541 = otvalor541;
	}

	public String getOtvalor542() {
		return otvalor542;
	}

	public void setOtvalor542(String otvalor542) {
		this.otvalor542 = otvalor542;
	}

	public String getOtvalor543() {
		return otvalor543;
	}

	public void setOtvalor543(String otvalor543) {
		this.otvalor543 = otvalor543;
	}

	public String getOtvalor544() {
		return otvalor544;
	}

	public void setOtvalor544(String otvalor544) {
		this.otvalor544 = otvalor544;
	}

	public String getOtvalor545() {
		return otvalor545;
	}

	public void setOtvalor545(String otvalor545) {
		this.otvalor545 = otvalor545;
	}

	public String getOtvalor546() {
		return otvalor546;
	}

	public void setOtvalor546(String otvalor546) {
		this.otvalor546 = otvalor546;
	}

	public String getOtvalor547() {
		return otvalor547;
	}

	public void setOtvalor547(String otvalor547) {
		this.otvalor547 = otvalor547;
	}

	public String getOtvalor548() {
		return otvalor548;
	}

	public void setOtvalor548(String otvalor548) {
		this.otvalor548 = otvalor548;
	}

	public String getOtvalor549() {
		return otvalor549;
	}

	public void setOtvalor549(String otvalor549) {
		this.otvalor549 = otvalor549;
	}

	public String getOtvalor550() {
		return otvalor550;
	}

	public void setOtvalor550(String otvalor550) {
		this.otvalor550 = otvalor550;
	}

	public String getOtvalor551() {
		return otvalor551;
	}

	public void setOtvalor551(String otvalor551) {
		this.otvalor551 = otvalor551;
	}

	public String getOtvalor552() {
		return otvalor552;
	}

	public void setOtvalor552(String otvalor552) {
		this.otvalor552 = otvalor552;
	}

	public String getOtvalor553() {
		return otvalor553;
	}

	public void setOtvalor553(String otvalor553) {
		this.otvalor553 = otvalor553;
	}

	public String getOtvalor554() {
		return otvalor554;
	}

	public void setOtvalor554(String otvalor554) {
		this.otvalor554 = otvalor554;
	}

	public String getOtvalor555() {
		return otvalor555;
	}

	public void setOtvalor555(String otvalor555) {
		this.otvalor555 = otvalor555;
	}

	public String getOtvalor556() {
		return otvalor556;
	}

	public void setOtvalor556(String otvalor556) {
		this.otvalor556 = otvalor556;
	}

	public String getOtvalor557() {
		return otvalor557;
	}

	public void setOtvalor557(String otvalor557) {
		this.otvalor557 = otvalor557;
	}

	public String getOtvalor558() {
		return otvalor558;
	}

	public void setOtvalor558(String otvalor558) {
		this.otvalor558 = otvalor558;
	}

	public String getOtvalor559() {
		return otvalor559;
	}

	public void setOtvalor559(String otvalor559) {
		this.otvalor559 = otvalor559;
	}

	public String getOtvalor560() {
		return otvalor560;
	}

	public void setOtvalor560(String otvalor560) {
		this.otvalor560 = otvalor560;
	}

	public String getOtvalor561() {
		return otvalor561;
	}

	public void setOtvalor561(String otvalor561) {
		this.otvalor561 = otvalor561;
	}

	public String getOtvalor562() {
		return otvalor562;
	}

	public void setOtvalor562(String otvalor562) {
		this.otvalor562 = otvalor562;
	}

	public String getOtvalor563() {
		return otvalor563;
	}

	public void setOtvalor563(String otvalor563) {
		this.otvalor563 = otvalor563;
	}

	public String getOtvalor564() {
		return otvalor564;
	}

	public void setOtvalor564(String otvalor564) {
		this.otvalor564 = otvalor564;
	}

	public String getOtvalor565() {
		return otvalor565;
	}

	public void setOtvalor565(String otvalor565) {
		this.otvalor565 = otvalor565;
	}

	public String getOtvalor566() {
		return otvalor566;
	}

	public void setOtvalor566(String otvalor566) {
		this.otvalor566 = otvalor566;
	}

	public String getOtvalor567() {
		return otvalor567;
	}

	public void setOtvalor567(String otvalor567) {
		this.otvalor567 = otvalor567;
	}

	public String getOtvalor568() {
		return otvalor568;
	}

	public void setOtvalor568(String otvalor568) {
		this.otvalor568 = otvalor568;
	}

	public String getOtvalor569() {
		return otvalor569;
	}

	public void setOtvalor569(String otvalor569) {
		this.otvalor569 = otvalor569;
	}

	public String getOtvalor570() {
		return otvalor570;
	}

	public void setOtvalor570(String otvalor570) {
		this.otvalor570 = otvalor570;
	}

	public String getOtvalor571() {
		return otvalor571;
	}

	public void setOtvalor571(String otvalor571) {
		this.otvalor571 = otvalor571;
	}

	public String getOtvalor572() {
		return otvalor572;
	}

	public void setOtvalor572(String otvalor572) {
		this.otvalor572 = otvalor572;
	}

	public String getOtvalor573() {
		return otvalor573;
	}

	public void setOtvalor573(String otvalor573) {
		this.otvalor573 = otvalor573;
	}

	public String getOtvalor574() {
		return otvalor574;
	}

	public void setOtvalor574(String otvalor574) {
		this.otvalor574 = otvalor574;
	}

	public String getOtvalor575() {
		return otvalor575;
	}

	public void setOtvalor575(String otvalor575) {
		this.otvalor575 = otvalor575;
	}

	public String getOtvalor576() {
		return otvalor576;
	}

	public void setOtvalor576(String otvalor576) {
		this.otvalor576 = otvalor576;
	}

	public String getOtvalor577() {
		return otvalor577;
	}

	public void setOtvalor577(String otvalor577) {
		this.otvalor577 = otvalor577;
	}

	public String getOtvalor578() {
		return otvalor578;
	}

	public void setOtvalor578(String otvalor578) {
		this.otvalor578 = otvalor578;
	}

	public String getOtvalor579() {
		return otvalor579;
	}

	public void setOtvalor579(String otvalor579) {
		this.otvalor579 = otvalor579;
	}

	public String getOtvalor580() {
		return otvalor580;
	}

	public void setOtvalor580(String otvalor580) {
		this.otvalor580 = otvalor580;
	}

	public String getOtvalor581() {
		return otvalor581;
	}

	public void setOtvalor581(String otvalor581) {
		this.otvalor581 = otvalor581;
	}

	public String getOtvalor582() {
		return otvalor582;
	}

	public void setOtvalor582(String otvalor582) {
		this.otvalor582 = otvalor582;
	}

	public String getOtvalor583() {
		return otvalor583;
	}

	public void setOtvalor583(String otvalor583) {
		this.otvalor583 = otvalor583;
	}

	public String getOtvalor584() {
		return otvalor584;
	}

	public void setOtvalor584(String otvalor584) {
		this.otvalor584 = otvalor584;
	}

	public String getOtvalor585() {
		return otvalor585;
	}

	public void setOtvalor585(String otvalor585) {
		this.otvalor585 = otvalor585;
	}

	public String getOtvalor586() {
		return otvalor586;
	}

	public void setOtvalor586(String otvalor586) {
		this.otvalor586 = otvalor586;
	}

	public String getOtvalor587() {
		return otvalor587;
	}

	public void setOtvalor587(String otvalor587) {
		this.otvalor587 = otvalor587;
	}

	public String getOtvalor588() {
		return otvalor588;
	}

	public void setOtvalor588(String otvalor588) {
		this.otvalor588 = otvalor588;
	}

	public String getOtvalor589() {
		return otvalor589;
	}

	public void setOtvalor589(String otvalor589) {
		this.otvalor589 = otvalor589;
	}

	public String getOtvalor590() {
		return otvalor590;
	}

	public void setOtvalor590(String otvalor590) {
		this.otvalor590 = otvalor590;
	}

	public String getOtvalor591() {
		return otvalor591;
	}

	public void setOtvalor591(String otvalor591) {
		this.otvalor591 = otvalor591;
	}

	public String getOtvalor592() {
		return otvalor592;
	}

	public void setOtvalor592(String otvalor592) {
		this.otvalor592 = otvalor592;
	}

	public String getOtvalor593() {
		return otvalor593;
	}

	public void setOtvalor593(String otvalor593) {
		this.otvalor593 = otvalor593;
	}

	public String getOtvalor594() {
		return otvalor594;
	}

	public void setOtvalor594(String otvalor594) {
		this.otvalor594 = otvalor594;
	}

	public String getOtvalor595() {
		return otvalor595;
	}

	public void setOtvalor595(String otvalor595) {
		this.otvalor595 = otvalor595;
	}

	public String getOtvalor596() {
		return otvalor596;
	}

	public void setOtvalor596(String otvalor596) {
		this.otvalor596 = otvalor596;
	}

	public String getOtvalor597() {
		return otvalor597;
	}

	public void setOtvalor597(String otvalor597) {
		this.otvalor597 = otvalor597;
	}

	public String getOtvalor598() {
		return otvalor598;
	}

	public void setOtvalor598(String otvalor598) {
		this.otvalor598 = otvalor598;
	}

	public String getOtvalor599() {
		return otvalor599;
	}

	public void setOtvalor599(String otvalor599) {
		this.otvalor599 = otvalor599;
	}

	public String getOtvalor600() {
		return otvalor600;
	}

	public void setOtvalor600(String otvalor600) {
		this.otvalor600 = otvalor600;
	}

	public String getOtvalor601() {
		return otvalor601;
	}

	public void setOtvalor601(String otvalor601) {
		this.otvalor601 = otvalor601;
	}

	public String getOtvalor602() {
		return otvalor602;
	}

	public void setOtvalor602(String otvalor602) {
		this.otvalor602 = otvalor602;
	}

	public String getOtvalor603() {
		return otvalor603;
	}

	public void setOtvalor603(String otvalor603) {
		this.otvalor603 = otvalor603;
	}

	public String getOtvalor604() {
		return otvalor604;
	}

	public void setOtvalor604(String otvalor604) {
		this.otvalor604 = otvalor604;
	}

	public String getOtvalor605() {
		return otvalor605;
	}

	public void setOtvalor605(String otvalor605) {
		this.otvalor605 = otvalor605;
	}

	public String getOtvalor606() {
		return otvalor606;
	}

	public void setOtvalor606(String otvalor606) {
		this.otvalor606 = otvalor606;
	}

	public String getOtvalor607() {
		return otvalor607;
	}

	public void setOtvalor607(String otvalor607) {
		this.otvalor607 = otvalor607;
	}

	public String getOtvalor608() {
		return otvalor608;
	}

	public void setOtvalor608(String otvalor608) {
		this.otvalor608 = otvalor608;
	}

	public String getOtvalor609() {
		return otvalor609;
	}

	public void setOtvalor609(String otvalor609) {
		this.otvalor609 = otvalor609;
	}

	public String getOtvalor610() {
		return otvalor610;
	}

	public void setOtvalor610(String otvalor610) {
		this.otvalor610 = otvalor610;
	}

	public String getOtvalor611() {
		return otvalor611;
	}

	public void setOtvalor611(String otvalor611) {
		this.otvalor611 = otvalor611;
	}

	public String getOtvalor612() {
		return otvalor612;
	}

	public void setOtvalor612(String otvalor612) {
		this.otvalor612 = otvalor612;
	}

	public String getOtvalor613() {
		return otvalor613;
	}

	public void setOtvalor613(String otvalor613) {
		this.otvalor613 = otvalor613;
	}

	public String getOtvalor614() {
		return otvalor614;
	}

	public void setOtvalor614(String otvalor614) {
		this.otvalor614 = otvalor614;
	}

	public String getOtvalor615() {
		return otvalor615;
	}

	public void setOtvalor615(String otvalor615) {
		this.otvalor615 = otvalor615;
	}

	public String getOtvalor616() {
		return otvalor616;
	}

	public void setOtvalor616(String otvalor616) {
		this.otvalor616 = otvalor616;
	}

	public String getOtvalor617() {
		return otvalor617;
	}

	public void setOtvalor617(String otvalor617) {
		this.otvalor617 = otvalor617;
	}

	public String getOtvalor618() {
		return otvalor618;
	}

	public void setOtvalor618(String otvalor618) {
		this.otvalor618 = otvalor618;
	}

	public String getOtvalor619() {
		return otvalor619;
	}

	public void setOtvalor619(String otvalor619) {
		this.otvalor619 = otvalor619;
	}

	public String getOtvalor620() {
		return otvalor620;
	}

	public void setOtvalor620(String otvalor620) {
		this.otvalor620 = otvalor620;
	}

	public String getOtvalor621() {
		return otvalor621;
	}

	public void setOtvalor621(String otvalor621) {
		this.otvalor621 = otvalor621;
	}

	public String getOtvalor622() {
		return otvalor622;
	}

	public void setOtvalor622(String otvalor622) {
		this.otvalor622 = otvalor622;
	}

	public String getOtvalor623() {
		return otvalor623;
	}

	public void setOtvalor623(String otvalor623) {
		this.otvalor623 = otvalor623;
	}

	public String getOtvalor624() {
		return otvalor624;
	}

	public void setOtvalor624(String otvalor624) {
		this.otvalor624 = otvalor624;
	}

	public String getOtvalor625() {
		return otvalor625;
	}

	public void setOtvalor625(String otvalor625) {
		this.otvalor625 = otvalor625;
	}

	public String getOtvalor626() {
		return otvalor626;
	}

	public void setOtvalor626(String otvalor626) {
		this.otvalor626 = otvalor626;
	}

	public String getOtvalor627() {
		return otvalor627;
	}

	public void setOtvalor627(String otvalor627) {
		this.otvalor627 = otvalor627;
	}

	public String getOtvalor628() {
		return otvalor628;
	}

	public void setOtvalor628(String otvalor628) {
		this.otvalor628 = otvalor628;
	}

	public String getOtvalor629() {
		return otvalor629;
	}

	public void setOtvalor629(String otvalor629) {
		this.otvalor629 = otvalor629;
	}

	public String getOtvalor630() {
		return otvalor630;
	}

	public void setOtvalor630(String otvalor630) {
		this.otvalor630 = otvalor630;
	}

	public String getOtvalor631() {
		return otvalor631;
	}

	public void setOtvalor631(String otvalor631) {
		this.otvalor631 = otvalor631;
	}

	public String getOtvalor632() {
		return otvalor632;
	}

	public void setOtvalor632(String otvalor632) {
		this.otvalor632 = otvalor632;
	}

	public String getOtvalor633() {
		return otvalor633;
	}

	public void setOtvalor633(String otvalor633) {
		this.otvalor633 = otvalor633;
	}

	public String getOtvalor634() {
		return otvalor634;
	}

	public void setOtvalor634(String otvalor634) {
		this.otvalor634 = otvalor634;
	}

	public String getOtvalor635() {
		return otvalor635;
	}

	public void setOtvalor635(String otvalor635) {
		this.otvalor635 = otvalor635;
	}

	public String getOtvalor636() {
		return otvalor636;
	}

	public void setOtvalor636(String otvalor636) {
		this.otvalor636 = otvalor636;
	}

	public String getOtvalor637() {
		return otvalor637;
	}

	public void setOtvalor637(String otvalor637) {
		this.otvalor637 = otvalor637;
	}

	public String getOtvalor638() {
		return otvalor638;
	}

	public void setOtvalor638(String otvalor638) {
		this.otvalor638 = otvalor638;
	}

	public String getOtvalor639() {
		return otvalor639;
	}

	public void setOtvalor639(String otvalor639) {
		this.otvalor639 = otvalor639;
	}

	public String getOtvalor640() {
		return otvalor640;
	}

	public void setOtvalor640(String otvalor640) {
		this.otvalor640 = otvalor640;
	}
	
	
}