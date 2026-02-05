import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        VStack(spacing: 16) {
            Text("Movie Database")
                .font(.largeTitle)
                .fontWeight(.bold)

            Text("""
                Your task is to implement the movie search UI here.

                Use the provided shared module with:
                - SearchMoviesUseCase
                - GetMovieDetailsUseCase
                - SelectMovieUseCase
                - GetRecentSelectionsUseCase
                """)
                .font(.body)
                .multilineTextAlignment(.center)
                .padding(.horizontal)
        }
        .padding()
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
