import SwiftUI

private struct MovieSuggestion: Decodable {
    let id: Int
    let name: String
    let year: Int
    let thumbnailUrl: String
}

@MainActor
final class FrontPageViewModel: ObservableObject {
    @Published var query: String = ""
    @Published var suggestions: [String] = []

    private var searchTask: Task<Void, Never>?

    func queryChanged(_ value: String) {
        query = value
        searchTask?.cancel()

        let trimmed = value.trimmingCharacters(in: .whitespacesAndNewlines)
        if trimmed.isEmpty {
            suggestions = []
            return
        }

        searchTask = Task {
            do {
                try await Task.sleep(nanoseconds: 500_000_000)
            } catch is CancellationError {
                return
            } catch {
                return
            }

            guard !Task.isCancelled else {
                return
            }

            await fetchSuggestions(query: trimmed)
        }
    }

    func selectSuggestion(_ suggestion: String) {
        query = suggestion
        suggestions = [suggestion]
    }

    private func fetchSuggestions(query: String) async {
        do {
            let encodedQuery = query.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
            guard let url = URL(string: "http://localhost:8080/api/movies/search?q=\(encodedQuery)") else {
                suggestions = []
                return
            }

            let (data, _) = try await URLSession.shared.data(from: url)
            let movies = try JSONDecoder().decode([MovieSuggestion].self, from: data)

            guard self.query.trimmingCharacters(in: .whitespacesAndNewlines) == query else {
                return
            }

            suggestions = Array(movies.prefix(5).map { $0.name })
        } catch is CancellationError {
            return
        } catch {
            suggestions = []
        }
    }
}

struct ContentView: View {
    @StateObject private var viewModel = FrontPageViewModel()

    var body: some View {
        VStack(alignment: .leading, spacing: 16) {
            Text("Beatgrid Movies")
                .font(.largeTitle)
                .fontWeight(.semibold)

            TextField("Search movies", text: $viewModel.query)
                .textFieldStyle(.roundedBorder)
                .onChange(of: viewModel.query) { newValue in
                    viewModel.queryChanged(newValue)
                }

            if !viewModel.query.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty && !viewModel.suggestions.isEmpty {
                VStack(spacing: 0) {
                    ForEach(viewModel.suggestions, id: \.self) { suggestion in
                        Button {
                            viewModel.selectSuggestion(suggestion)
                        } label: {
                            HStack {
                                Text(suggestion)
                                    .foregroundColor(.primary)
                                Spacer()
                            }
                            .padding(.vertical, 8)
                            .padding(.horizontal, 12)
                        }
                        .buttonStyle(.plain)

                        if suggestion != viewModel.suggestions.last {
                            Divider()
                        }
                    }
                }
                .background(Color(uiColor: .secondarySystemBackground))
                .clipShape(RoundedRectangle(cornerRadius: 10))
            }

            Spacer()
        }
        .padding(.horizontal, 24)
        .padding(.vertical, 32)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
