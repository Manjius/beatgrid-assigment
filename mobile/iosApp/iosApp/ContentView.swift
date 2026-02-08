import SwiftUI
import shared

@MainActor
final class FrontPageViewModel: ObservableObject {
    @Published var state = FrontPageState(
        query: "",
        logoTitle: "Beatgrid Movies",
        suggestions: [],
        searchConnectionFailed: false,
        recentSelections: [],
        recentsConnectionFailed: false,
        selectedMovie: nil,
        isLoadingMovie: false
    )

    private let bridge = IosFrontPageBridge()

    init() {
        bridge.startObserving { [weak self] newState in
            self?.state = newState
        }
    }

    deinit {
        bridge.clear()
    }

    func onQueryChanged(_ query: String) {
        bridge.onQueryChanged(query: query)
    }

    func onSuggestionSelected(_ suggestion: MovieSearchResult) {
        bridge.onSuggestionSelected(suggestion: suggestion)
    }

    func onRecentSelected(_ movieId: Int32) {
        bridge.onRecentSelected(movieId: Int(movieId))
    }

    func onBack() {
        bridge.onBack()
    }
}

struct ContentView: View {
    @StateObject private var viewModel = FrontPageViewModel()

    var body: some View {
        if let selectedMovie = viewModel.state.selectedMovie {
            MovieDetailsView(movie: selectedMovie) {
                viewModel.onBack()
            }
        } else {
            FrontPageContent(viewModel: viewModel)
        }
    }
}

private struct FrontPageContent: View {
    @ObservedObject var viewModel: FrontPageViewModel

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Text(viewModel.state.logoTitle)
                    .font(.largeTitle)
                    .fontWeight(.semibold)

                TextField(
                    "Search movies",
                    text: Binding(
                        get: { viewModel.state.query },
                        set: { viewModel.onQueryChanged($0) }
                    )
                )
                .textFieldStyle(.roundedBorder)

                if viewModel.state.isDropdownVisible {
                    VStack(spacing: 0) {
                        ForEach(viewModel.state.suggestions, id: \.id) { suggestion in
                            Button {
                                viewModel.onSuggestionSelected(suggestion)
                            } label: {
                                HStack {
                                    Text("\(suggestion.name) (\(suggestion.year))")
                                        .foregroundColor(.primary)
                                    Spacer()
                                }
                                .padding(.vertical, 8)
                                .padding(.horizontal, 12)
                            }
                            .buttonStyle(.plain)

                            if suggestion.id != viewModel.state.suggestions.last?.id {
                                Divider()
                            }
                        }
                    }
                    .background(Color(uiColor: .secondarySystemBackground))
                    .clipShape(RoundedRectangle(cornerRadius: 10))
                }

                if viewModel.state.searchConnectionFailed && !viewModel.state.query.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                    Text("Can't connect to the database to search, sorry :(")
                }

                if viewModel.state.isLoadingMovie {
                    Text("Loading movie details...")
                }

                if viewModel.state.recentsConnectionFailed {
                    Text("We can't load thumbnails from the database, sorry :(")
                        .padding(.top, 8)
                }

                if !viewModel.state.recentSelections.isEmpty {
                    Text("Recently Selected")
                        .font(.title3)
                        .fontWeight(.medium)
                        .padding(.top, 12)

                    LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible()), GridItem(.flexible())], spacing: 8) {
                        ForEach(viewModel.state.recentSelections, id: \.movieId) { recent in
                            Button {
                                viewModel.onRecentSelected(recent.movieId)
                            } label: {
                                AsyncImage(url: URL(string: recent.thumbnailUrl)) { image in
                                    image
                                        .resizable()
                                        .scaledToFill()
                                } placeholder: {
                                    Color.gray.opacity(0.2)
                                }
                                .frame(maxWidth: .infinity)
                                .aspectRatio(2.0 / 3.0, contentMode: .fit)
                                .clipShape(RoundedRectangle(cornerRadius: 8))
                            }
                            .buttonStyle(.plain)
                        }
                    }
                }
            }
            .padding(.horizontal, 24)
            .padding(.vertical, 32)
        }
    }
}

private struct MovieDetailsView: View {
    let movie: Movie
    let onBack: () -> Void

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                Button("Back", action: onBack)

                Text(movie.name)
                    .font(.largeTitle)
                    .fontWeight(.bold)

                Text("\(movie.year)")
                    .font(.title3)
                    .foregroundStyle(.secondary)

                Text(movie.description_)
                    .font(.body)

                if !movie.genres.isEmpty {
                    Text("Genres: \(movie.genres.map { $0.name }.joined(separator: ", "))")
                        .font(.callout)
                        .foregroundStyle(.secondary)
                }
            }
            .padding(.horizontal, 24)
            .padding(.vertical, 32)
        }
    }
}
