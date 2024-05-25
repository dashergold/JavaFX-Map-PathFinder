
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GraphTest {
	//
	private static final int MAJOR_VERSION = 2;
	private static final int MINOR_VERSION = 3;
	private static final String UPDATED_DATE = "2023-04-19";
	//
	private static final String ILLEGAL_STATE_EXCEPTION_BORDE_HA_KASTATS = "Fel: undantaget IllegalStateException borde ha kastats.";
	private static final String GET_NODES_INCORRECT_CONTENT = "Fel: samlingen getNodes returnerar innehåller inte rätt noder.";
	private static final String NO_SUCH_ELEMENT_EXCEPTION_BORDE_HA_KASTATS = "Fel: undantaget NoSuchElementException borde ha kastats.";
	private static final String ILLEGAL_ARGUMENT_EXCEPTION_BORDE_HA_KASTATS = "Fel: IllegalArgumentException borde ha kastats.";
	private static final String INVALID_NODE_1 = "ZZZZ";
	private static final String INVALID_NODE_2 = "YYYY";
	private static final String INGET_UNDANTAG_BORDE_HA_KASTATS = "Fel: borde inte ha kastat något undantag.";
	private static final String ERROR_MISSING_EDGE = "Fel: bågen borde ha funnits.";
	private static final String ERROR_NONMISSING_EDGE = "Fel: bågen borde inte finnas.";
	private static final String ERROR_NONMISSING_PATH = "Fel: det borde inte ha funnits någon väg mellan noderna.";
	private static final String VALID_NODE_NOT_CONNECTED = "X";
	private static final String VALID_NODE_1 = "A";
	private static final String VALID_NODE_2 = "B";
	private static final String VALID_NODE_3 = "G";
	private static final String[] STANDARD_NODES = {VALID_NODE_1, VALID_NODE_2, "C", "D", "E", "F", VALID_NODE_3, "H", "I", "J", "X"};

	private final Graph<String> graph = new ListGraph<>();

	@Test
	@Order(0)
	@DisplayName("Information")
	void __version() {
		System.out.printf("Test version %d.%d (%s)%n", MAJOR_VERSION, MINOR_VERSION, UPDATED_DATE);
	}

	private void add(String... nodes) {
		for (String node : nodes) {
			graph.add(node);
			assertTrue(graph.getNodes().contains(node), "Fel: kunde inte lägga till nod med add.");
		}
	}

	private void addExampleNodes() {
		add(STANDARD_NODES);
	}

	private void connect(String node1, String node2, String name, int cost) {
		graph.connect(node1, node2, name, cost);
		assertNotNull(graph.getEdgeBetween(node1, node2), "Fel: försökte bekräfta connect med getEdgeBetween men det gick inte.");
		assertEquals(cost, graph.getEdgeBetween(node1, node2).getWeight(), "Fel: försökte bekräfta connect med getEdgeBetween men det gick inte.");
		assertEquals(cost, graph.getEdgeBetween(node2, node1).getWeight(), "Fel: försökte bekräfta connect med getEdgeBetween men det gick inte.");
	}

	private void createExampleGraph() {
		addExampleNodes();

//		connect(VALID_NODE_1, VALID_NODE_1, "A -> A", 1);
		connect("A", "G", "A -> G", 3);
		connect("G", "B", "G -> B", 28);
		connect("B", "F", "B -> F", 5);
//		connect("F", "F", "F -> F", 3);
		connect("F", "H", "F -> H", 1);
		connect("H", "D", "H -> D", 1);
		connect("H", "I", "H -> I", 3);
		connect("D", "I", "D -> I", 1);
		connect("B", "D", "B -> D", 2);
		connect("B", "C", "B -> C", 3);
		connect("C", "D", "C -> D", 5);
		connect("E", "C", "E -> C", 2);
		connect("E", "D", "E -> D", 2);
		connect("J", "D", "J -> D", 5);
	}

	@BeforeEach
	void setUp() {

	}

	@Test
	@Order(10)
	@DisplayName("Testar att grafen är generisk och accepterar olika nodtyper.")
	void test00_genericGraphAcceptsDifferentNodeTypes() {
		assertDoesNotThrow(() -> {
			Graph<Integer> integerGraph = new ListGraph<>();
			Graph<Character> characterGraph = new ListGraph<>();
		}, "Kunde inte skapa en graf med en generisk typ.");
	}

	@Test
	@Order(20)
	@DisplayName("Testar att en ny graf är tom.")
	void test00_newGraphIsEmptyGraph() {
		assertTrue(graph.getNodes().isEmpty(), "Fel: grafen borde ha varit tom men det var den inte.");
	}

	@ParameterizedTest
	@Order(30)
	@CsvSource({"D", "J", "K"})
	@DisplayName("Testar att lägga till noder med add.")
	void test01_add(String node) {
		graph.add(node);
		assertTrue(graph.getNodes().contains(node), "Fel: samlingen som getNodes returnerar innehåller inte den nya noden.");
	}

	@Test
	@Order(40)
	@DisplayName("Testar att ta bort nod som finns och har båge med remove.")
	void test04_remove_existing_node_with_edge() {

		createExampleGraph();

		assertNotNull(graph.getEdgeBetween(VALID_NODE_1, VALID_NODE_3), "Fel: grafen har inte en båge mellan noderna.");

		assertTrue(graph.getNodes().contains(VALID_NODE_3), "Fel: grafen innehåller inte noden innan remove.");

		graph.remove(VALID_NODE_3);

		assertFalse(graph.getNodes().contains(VALID_NODE_3), "Fel: grafen innehåller fortfarande noden efter remove.");

		assertThrows(NoSuchElementException.class, () -> graph.getEdgeBetween(VALID_NODE_1, VALID_NODE_3));

		assertEquals(Optional.empty(), graph.getEdgesFrom(VALID_NODE_1).stream().map(Edge<String>::getDestination).filter(d -> d.equals(VALID_NODE_3)).findFirst());
	}

	@Test
	@Order(42)
	@DisplayName("Testar att ta bort nod som inte finns.")
	void test04_remove_nonexisting_node() {
		assertThrows(NoSuchElementException.class, () -> graph.remove(INVALID_NODE_1), NO_SUCH_ELEMENT_EXCEPTION_BORDE_HA_KASTATS);
	}

	@Test
	@Order(50)
	@DisplayName("Testar getNodes.")
	void test05_getNodes() {
		createExampleGraph();

		var nodes = graph.getNodes();
		assertTrue(nodes.containsAll(Arrays.asList(STANDARD_NODES)), GET_NODES_INCORRECT_CONTENT);
	}

	@Test
	@Order(60)
	@DisplayName("Testar connect med noder som har en båge.")
	void test06_connect_existing_nodes_with_edge() {
		addExampleNodes();
		graph.connect(VALID_NODE_1, VALID_NODE_3, "A->G", 5);
		assertThrows(IllegalStateException.class, () -> graph.connect(VALID_NODE_1, VALID_NODE_3, "A->G", 5), ILLEGAL_STATE_EXCEPTION_BORDE_HA_KASTATS);
	}

	@Test
	@Order(62)
	@DisplayName("Testar connect med två noder som finns och inte har en båge.")
	void test06_connect_existing_nodes_without_edge() {
		addExampleNodes();
		assertNull(graph.getEdgeBetween(VALID_NODE_1, VALID_NODE_3), "Fel: bågen borde inte ha funnits innan connect.");
		graph.connect(VALID_NODE_1, VALID_NODE_3, "A->G", 5);
		assertNotNull(graph.getEdgeBetween(VALID_NODE_1, VALID_NODE_3), "Fel: bågen saknas efter connect.");
	}

	@Test
	@Order(64)
	@DisplayName("Testar connect med negativ vikt.")
	void test06_connect_negative_weight() {
		addExampleNodes();
		assertThrows(IllegalArgumentException.class, () -> graph.connect(VALID_NODE_1, VALID_NODE_3, "A->G", -5), ILLEGAL_ARGUMENT_EXCEPTION_BORDE_HA_KASTATS);
	}

	@Test
	@Order(66)
	@DisplayName("Testar connect med noder som inte finns.")
	void test06_connect_non_existing_nodes() {
		addExampleNodes();
		assertThrows(NoSuchElementException.class, () -> graph.connect(INVALID_NODE_1, VALID_NODE_3, "ZZZZ->G", 5), NO_SUCH_ELEMENT_EXCEPTION_BORDE_HA_KASTATS);
	}

	@Test
	@Order(70)
	@DisplayName("Testar disconnect av edge som finns (A-G).")
	void test07_disconnect() {
		createExampleGraph();

		assertNotNull(graph.getEdgeBetween(VALID_NODE_1, VALID_NODE_3), ERROR_MISSING_EDGE);
		graph.disconnect(VALID_NODE_1, VALID_NODE_3);
		assertNull(graph.getEdgeBetween(VALID_NODE_1, VALID_NODE_3), "Bågen A-G kunde inte tas bort.");
		assertNull(graph.getEdgeBetween(VALID_NODE_3, VALID_NODE_1), "Bågen G-A kunde inte tas bort.");
	}

	@Test
	@Order(71)
	@DisplayName("Testar disconnect av nod som inte finns.")
	void test07_disconnect_missing_node() {
		createExampleGraph();
		assertThrows(NoSuchElementException.class, () -> graph.disconnect(INVALID_NODE_1, VALID_NODE_3), NO_SUCH_ELEMENT_EXCEPTION_BORDE_HA_KASTATS);
	}

	@Test
	@Order(72)
	@DisplayName("Testar disconnect av edge som inte finns.")
	void test07_disconnect_no_connection() {
		createExampleGraph();
		assertThrows(IllegalStateException.class, () -> graph.disconnect(VALID_NODE_1, VALID_NODE_2), ILLEGAL_STATE_EXCEPTION_BORDE_HA_KASTATS);
	}

	@Test
	@Order(80)
	@DisplayName("Testar getEdgeBetween av edge som finns (A-G).")
	void test08_getEdgeBetween_existing_edge() {
		createExampleGraph();

		assertNotNull(graph.getEdgeBetween(VALID_NODE_1, VALID_NODE_3), "Fel. Det borde ha funnits en båge.");
		assertEquals("A -> G", graph.getEdgeBetween(VALID_NODE_1, VALID_NODE_3).getName());
	}

	@Test
	@Order(82)
	@DisplayName("Testar getEdgeBetween när nod saknas.")
	void test08_getEdgeBetween_missing_node() {
		createExampleGraph();
		assertThrows(NoSuchElementException.class, () -> graph.getEdgeBetween(VALID_NODE_1, INVALID_NODE_1), NO_SUCH_ELEMENT_EXCEPTION_BORDE_HA_KASTATS);
	}

	@Test
	@Order(84)
	@DisplayName("Testar getEdgeBetween av edge som inte finns.")
	void test08_getEdgeBetween_nonexisting_edge() {
		createExampleGraph();
		assertNull(graph.getEdgeBetween(VALID_NODE_1, "D"), ERROR_NONMISSING_EDGE);
	}

	@Test
	@Order(90)
	@DisplayName("Testar getEdgesFrom för nod som finns och har bågar.")
	void test09_getEdgesFrom_existing_node_with_edges() {
		createExampleGraph();

		var actualEdges = graph.getEdgesFrom(VALID_NODE_3);
		assertEquals(2, actualEdges.size());

		var actualStrings = actualEdges.stream().map(Edge<String>::getName).collect(Collectors.toSet());
		actualStrings.addAll(actualEdges.stream().map(Edge<String>::getDestination).collect(Collectors.toSet()));
		actualStrings.addAll(actualEdges.stream().map(stringEdge -> String.valueOf(stringEdge.getWeight())).collect(Collectors.toSet()));
		actualStrings.add(VALID_NODE_3);

		var expectedStrings = new HashSet<>(Set.of(VALID_NODE_1, "B", VALID_NODE_3, "28", "3"));
		var match = actualStrings.containsAll(expectedStrings);

		expectedStrings.removeAll(actualStrings);

		assertTrue(match, "Det saknades information om bågar.\nnLetade efter: " + expectedStrings);
	}

	@Test
	@Order(92)
	@DisplayName("Testar getEdgesFrom för nod som finns och saknar bågar.")
	void test09_getEdgesFrom_existing_node_without_edges() {
		createExampleGraph();

		var actualEdges = graph.getEdgesFrom(VALID_NODE_NOT_CONNECTED);
		assertEquals(0, actualEdges.size(), "Fel: noden ska inte ha några bågar.");
	}

	@Test
	@Order(94)
	@DisplayName("Testar getEdgesFrom för nod som inte finns.")
	void test09_getEdgesFrom_non_existing_node() {
		assertThrows(NoSuchElementException.class, () -> graph.getEdgesFrom(INVALID_NODE_1), NO_SUCH_ELEMENT_EXCEPTION_BORDE_HA_KASTATS);
	}

	@Test
	@Order(100)
	@DisplayName("Testar pathExists för noder som finns och har en väg mellan sig.")
	void test10_pathExists_existing_nodes_with_valid_path() {
		createExampleGraph();

		var validPath = graph.pathExists(VALID_NODE_1, "C");
		assertTrue(validPath, "Fel: det borde ha funnits en väg mellan noderna.");
	}

	@Test
	@Order(102)
	@DisplayName("Testar pathExists för noder som finns och men inte har en väg mellan sig.")
	void test10_pathExists_existing_nodes_without_valid_path() {
		createExampleGraph();
		var invalidPath = graph.pathExists(VALID_NODE_1, VALID_NODE_NOT_CONNECTED);
		assertFalse(invalidPath, ERROR_NONMISSING_PATH);
	}

	@Test
	@Order(104)
	@DisplayName("Testar pathExists för noder som inte finns.")
	void test10_pathExists_non_existing_nodes() {
		createExampleGraph();
		assertDoesNotThrow(() -> graph.pathExists(INVALID_NODE_1, INVALID_NODE_2), INGET_UNDANTAG_BORDE_HA_KASTATS + " (ogiltig1, ogiltig2)");
		assertDoesNotThrow(() -> graph.pathExists(VALID_NODE_1, INVALID_NODE_2), INGET_UNDANTAG_BORDE_HA_KASTATS + " (giltig, ogiltig)");
		assertDoesNotThrow(() -> graph.pathExists(INVALID_NODE_1, VALID_NODE_1), INGET_UNDANTAG_BORDE_HA_KASTATS + " (ogiltig, giltig)");
		assertDoesNotThrow(() -> graph.pathExists(INVALID_NODE_2, INVALID_NODE_1), INGET_UNDANTAG_BORDE_HA_KASTATS + " (ogiltig2, ogiltig1)");
		assertFalse(graph.pathExists(INVALID_NODE_1, INVALID_NODE_2));
		assertFalse(graph.pathExists(VALID_NODE_1, INVALID_NODE_1));
		assertFalse(graph.pathExists(VALID_NODE_1, VALID_NODE_NOT_CONNECTED));
	}

	@Test
	@Order(110)
	@DisplayName("Testar getPath för noder som har en väg.")
	void test11_getPath_existing_path() {
		createExampleGraph();
		var validPath = graph.getPath(VALID_NODE_1, VALID_NODE_2);
		var cost = validPath.stream().map(Edge<String>::getWeight).reduce(0, Integer::sum);
		assertEquals(2, validPath.size(), "Fel: den enda vägen mellan noderna borde vara 2 steg lång.");
		assertEquals(31, cost, "Fel: den enda vägen mellan noderna borde kosta 31.");
	}

	@Test
	@Order(110)
	@DisplayName("Testar getPath för noder som inte har en väg.")
	void test11_getPath_non_existing_path() {
		createExampleGraph();

		var invalidPath = graph.getPath(VALID_NODE_1, VALID_NODE_NOT_CONNECTED);
		assertNull(invalidPath, ERROR_NONMISSING_PATH);
	}

	@Test
	@Order(122)
	@DisplayName("Testar setConnectionWeight för nod som inte finns.")
	void test12_setConnectionWeight_invalid_node() {
		createExampleGraph();
		assertThrows(NoSuchElementException.class, () -> graph.setConnectionWeight(INVALID_NODE_1, VALID_NODE_3, 1), NO_SUCH_ELEMENT_EXCEPTION_BORDE_HA_KASTATS);
	}

	@Test
	@Order(120)
	@DisplayName("Testar setConnectionWeight med ogiltig vikt.")
	void test12_setConnectionWeight_invalid_weight() {
		createExampleGraph();
		assertThrows(IllegalArgumentException.class, () -> graph.setConnectionWeight(VALID_NODE_1, VALID_NODE_3, -1), ILLEGAL_ARGUMENT_EXCEPTION_BORDE_HA_KASTATS);
	}

	@Test
	@Order(124)
	@DisplayName("Testar setConnectionWeight för båge som finns.")
	void test12_setConnectionWeight_valid_edge() {
		createExampleGraph();

		// pre condition
		var edge1 = graph.getEdgeBetween(VALID_NODE_1, VALID_NODE_3);
		assertNotNull(edge1, "Fel: bågen borde ha funnits. ( A -> G )");
		assertEquals(3, edge1.getWeight(), "Fel: fel vikt innan setConnectionWeight. ( A -> G )");

		var edge2 = graph.getEdgeBetween(VALID_NODE_3, VALID_NODE_1);
		assertNotNull(edge2, "Fel: bågen borde ha funnits. ( G -> A )");
		assertEquals(3, edge2.getWeight(), "Fel: fel vikt innan setConnectionWeight. ( G -> A )");

		graph.setConnectionWeight(VALID_NODE_1, VALID_NODE_3, 22);

		// post condition
		assertEquals(22, edge1.getWeight(), "Fel: fel vikt efter setConnectionWeight. ( A -> G )");
		assertEquals(22, edge2.getWeight(), "Fel: fel vikt efter setConnectionWeight. ( G -> A )");

		var validPath = graph.getPath(VALID_NODE_1, VALID_NODE_2);
		Assumptions.assumingThat(validPath != null, () -> {
			assert validPath != null;
			var cost = validPath.stream().map(Edge<String>::getWeight).reduce(0, Integer::sum);
			assertEquals(2, validPath.size(), "Fel: längd på path borde inte ha ändrats.");
			assertEquals(50, cost, "Fel: kostnad för path borde ha ändrats efter setConnectionWeight.");
		});
	}

	@Test
	@Order(130)
	@DisplayName("Testar Edge<String>::toString.")
	void test13_edgeToString_valid_edge() {
		createExampleGraph();
		var edge = graph.getEdgeBetween(VALID_NODE_1, VALID_NODE_3);
		var expected = "till G med A -> G tar 3";
		var actual = edge.toString().trim();
		assertEquals(expected, actual, String.format("Edge<String>::toString ser inte ut som förväntat. Borde ha varit:%n%s%nmen var:%n%s%n", expected, actual));
	}

	@Test
	@Order(140)
	@DisplayName("Testar Graph::toString.")
	void test14_graphToString() {
		createExampleGraph();
		var message = "Grafens toString saknar information.%nLetade efter följande ord:%n%s i strängen:%n%n%s%nmen något saknades.%n";

		var edges = new HashSet<String>();
		for (String node : graph.getNodes()) {
			for (Edge<String> stringEdge : graph.getEdgesFrom(node)) {
				edges.add(stringEdge.toString());
			}
		}

		var nodes = graph.getNodes();

		var toString = graph.toString();

		var containsNodes = nodes.stream().allMatch(toString::contains);
		var containsEdges = edges.stream().allMatch(toString::contains);

		var missingEdges = edges
				.stream()
				.filter(s -> !toString.contains(s))
				.collect(Collectors.toSet());

		if (!missingEdges.isEmpty())
			fail("Någon båge saknas i toString: " + missingEdges);

		var all = new HashSet<>(nodes);
		all.addAll(edges);
		assertTrue(containsNodes && containsEdges, String.format(message, all, toString));
	}
}