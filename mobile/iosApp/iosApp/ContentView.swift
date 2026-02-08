import SwiftUI
import shared

struct FrontPageContainerViewController: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        FrontPageViewControllerKt.FrontPageViewController()
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    var body: some View {
        FrontPageContainerViewController()
            .ignoresSafeArea(.keyboard)
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
